package mc.speedx.entity

import net.minecraft.entity.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Arm
import net.minecraft.util.Hand
import net.minecraft.util.math.*
import net.minecraft.world.World


class BikeEntity(entityType: EntityType<out LivingEntity>, world: World?) : LivingEntity(entityType, world) {
    private var lastForwardsSpeed = 0.0
    private var lastDrift = 0.0
    private var lastTurnAmount = 0.0f

    private var airTrackedSpeed = 0.0

    private var lastY = 0
    private var verticalGainTicks = 0

    var wheelPosition = 0.0f

    init {
        this.stepHeight = 1.0F
    }

    override fun getMainArm(): Arm? {
        return Arm.RIGHT
    }

    override fun equipStack(slot: EquipmentSlot?, stack: ItemStack?) {
        throw UnsupportedOperationException()
    }

    override fun damage(source: DamageSource?, amount: Float): Boolean {
        if (source != null) {
            if (source.name == "inWall" || source.name == "fall") {
                return false
            }
        }
        return super.damage(source, amount)
    }

    override fun interact(player: PlayerEntity, hand: Hand?): ActionResult? {
        if (isAlive) {
            if (hasPassengers()) {
                return ActionResult.PASS
            }
            if (!world.isClient()) {
                player.yaw = yaw
                player.pitch = pitch
                player.startRiding(this)
            }
            return ActionResult.success(world.isClient())
        }
        return ActionResult.PASS
    }

    override fun travel(movementInput: Vec3d) {
        if (isAlive) {
            if (hasPassengers() && primaryPassenger is LivingEntity) {
                // Update movement and rotation to that of the primary passenger
                val ent = primaryPassenger as LivingEntity

                var forwards = ent.forwardSpeed.toDouble()

                if (forwards <= 0.0f) {
                    // Going backwards is very slow
                    forwards *= backwardsSpeedMultiplier
                }

                if (isOnGround) {
                    if (forwards > 0.15) {
                        // Rotate at a higher rate
                        val expectedTurnAmount = ent.sidewaysSpeed * forwardsTurnSpeed
                        val actualTurnAmount =
                            MathHelper.lerp(turnDelta, lastTurnAmount.toDouble(), expectedTurnAmount.toDouble())
                                .toFloat()
                        lastTurnAmount = actualTurnAmount

                        yaw -= actualTurnAmount
                        prevYaw = yaw
                        ent.yaw -= actualTurnAmount
                    }
                    if (forwards < -0.05) {
                        // Rotate at a slower rate (Also reversed)
                        val expectedTurnAmount = ent.sidewaysSpeed * backwardsTurnSpeed
                        val actualTurnAmount =
                            MathHelper.lerp(turnDelta, lastTurnAmount.toDouble(), expectedTurnAmount.toDouble())
                                .toFloat()
                        lastTurnAmount = actualTurnAmount

                        yaw += actualTurnAmount
                        prevYaw = yaw
                        ent.yaw += actualTurnAmount
                    }
                } else {
                    lastTurnAmount = 0F
                }

                bodyYaw = yaw
                setRotation(yaw, pitch)

                val f: Float = MathHelper.wrapDegrees(ent.yaw - yaw)
                val g = MathHelper.clamp(f, -45.0f, 45.0f)
                ent.yaw += g - f

                flyingSpeed = movementSpeed * 0.2f

                val lerpedSpeed = if (lastForwardsSpeed < 0.95) {
                    MathHelper.lerp(startupSpeedDelta, lastForwardsSpeed, forwards)
                } else {
                    MathHelper.lerp(extraSpeedDelta, lastForwardsSpeed, forwards * extraSpeedMultiplier)
                }

                val drift = if (lerpedSpeed > driftSpeedMinimum) {
                    MathHelper.lerp(driftDelta, lastDrift, -ent.sidewaysSpeed.toDouble() * driftCapMultiplier)
                } else 0.0
                lastDrift = drift

                if (isLogicalSideForUpdatingMovement) {
                    movementSpeed = getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED).toFloat()

                    var moveSpeed = lerpedSpeed - (drift * driftSpeedReductionMultiplier)
                    if (isOnGround) {
                        airTrackedSpeed = moveSpeed
                    } else {
                        moveSpeed = airTrackedSpeed
                    }
                    super.travel(
                        Vec3d(
                            drift,
                            0.0,
                            moveSpeed
                        )
                    )

                    if (lastY != blockPos.y && isOnGround) {
                        verticalGainTicks += 1
                    } else if (verticalGainTicks > 0 && !isOnGround) {
                        setNoGravity(true)
                        verticalGainTicks -= 1
                    } else {
                        setNoGravity(false)
                    }
                    lastY = blockPos.y

                    super.travel(
                        Vec3d(
                            0.0,
                            if (hasNoGravity()) {
                                flyingSpeed = (movementSpeed * 0.6).toFloat()
                                verticalGainTicks.toDouble()
                            } else 0.0,
                            0.0
                        )
                    )

                    lastForwardsSpeed = lerpedSpeed
                } else if (ent is PlayerEntity) {
                    velocity = Vec3d.ZERO
                }
            } else {
                super.travel(movementInput)
            }
        }
    }

    override fun getPrimaryPassenger(): Entity? {
        return if (hasPassengers()) passengerList[0] else null
    }

    override fun isPushable(): Boolean {
        return false
    }

    override fun isAffectedBySplashPotions(): Boolean {
        return false
    }

    override fun getArmorItems(): MutableIterable<ItemStack> {
        return mutableListOf()
    }

    override fun getEquippedStack(slot: EquipmentSlot?): ItemStack {
        return ItemStack.EMPTY
    }

    override fun canHaveStatusEffect(effect: StatusEffectInstance?): Boolean {
        return false
    }

    override fun updatePassengerForDismount(passenger: LivingEntity): Vec3d? {
        lastForwardsSpeed = 0.0
        velocity = Vec3d.ZERO

        val dir = movementDirection
        if (dir.axis === Direction.Axis.Y) {
            // If moving vertically, delegate to super
            return super.updatePassengerForDismount(passenger)
        } else {
            val offsets = Dismounting.getDismountOffsets(dir)
            val currBlockPos = blockPos
            val targetBlockPos = BlockPos.Mutable()
            for (pose in passenger.poses) {
                val passengerBounds = passenger.getBoundingBox(pose)
                for (offset in offsets) {
                    targetBlockPos[currBlockPos.x + offset[0], currBlockPos.y] = currBlockPos.z + offset[1]
                    val height = world.getCollisionHeightAt(targetBlockPos)
                    if (Dismounting.canDismountInBlock(height)) {
                        val newPos = Vec3d.ofCenter(targetBlockPos, height)
                        if (Dismounting.canPlaceEntityAt(world, passenger, passengerBounds.offset(newPos))) {
                            passenger.pose = pose
                            return newPos
                        }
                    }
                }
            }
        }
        return super.updatePassengerForDismount(passenger)
    }

    override fun pushAwayFrom(entity: Entity?) {
        // No
    }

    override fun getHardCollisionBox(collidingEntity: Entity?): Box? {
        return Box(1.0, 1.0, 1.0, 1.0, 1.0, 1.0)
    }

    override fun setHeadYaw(headYaw: Float) {
        // no
    }

    override fun setYaw(yaw: Float) {
        // no
    }

    override fun updatePassengerPosition(passenger: Entity) {
        super.updatePassengerPosition(passenger)
        if (hasPassenger(passenger)) {
            // Translate 0.45 backwards, accounting for yaw
            val height = y + mountedHeightOffset + passenger.heightOffset - 0.15
            val backwardsOffset = Vec3d(0.0, 0.0, -0.45).rotateY(Math.toRadians(-bodyYaw.toDouble()).toFloat())
            passenger.updatePosition(x + backwardsOffset.getX(), height, z + backwardsOffset.getZ())
        }
    }

    override fun canClimb(): Boolean {
        return false
    }

    companion object {
        fun createBikeAttributes(): DefaultAttributeContainer.Builder? {
            val attributes = createLivingAttributes()
            attributes.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, baseSpeed)
            attributes.add(EntityAttributes.GENERIC_MAX_HEALTH, 1.0)
            return attributes
        }

        private const val baseSpeed = 0.3

        const val forwardsTurnSpeed = 4.5f
        const val backwardsTurnSpeed = 2.2f
        const val turnDelta = 0.22

        const val driftDelta = 0.07
        const val driftCapMultiplier = 1.7
        const val driftSpeedReductionMultiplier = 0.25
        const val driftSpeedMinimum = 1.3

        const val startupSpeedDelta = 0.17
        const val extraSpeedDelta = 0.08
        const val extraSpeedMultiplier = 1.5
        const val backwardsSpeedMultiplier = 0.2
    }
}