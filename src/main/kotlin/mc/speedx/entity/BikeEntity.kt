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
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World


class BikeEntity(entityType: EntityType<out LivingEntity>, world: World?) : LivingEntity(entityType, world) {
    private var lastForwardsSpeed = 0.0
    private var lastDrift = 0.0

    var wheelPosition = 0.0f

    init {
        this.stepHeight = 1.0F
    }

    override fun getMainArm(): Arm? {
        return Arm.RIGHT
    }

    override fun equipStack(slot: EquipmentSlot?, stack: ItemStack?) {
        TODO("Not yet implemented")
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
                    forwards *= 0.1f
                }

                if (forwards > 0.15) {
                    // Rotate at a higher rate
                    yaw -= ent.sidewaysSpeed * forwardsTurnSpeed
                    prevYaw = yaw
                    ent.yaw -= ent.sidewaysSpeed * forwardsTurnSpeed
                }
                if (forwards < -0.05) {
                    // Rotate at a slower rate (Also reversed)
                    yaw += ent.sidewaysSpeed * backwardsTurnSpeed
                    ent.yaw += ent.sidewaysSpeed * backwardsTurnSpeed
                }

                setRotation(yaw, pitch)
                flyingSpeed = movementSpeed * 0.3f
                val lerpedSpeed = MathHelper.lerp(speedDelta, lastForwardsSpeed, forwards)
                val drift = if (forwards > 0.9) {
                    MathHelper.lerp(driftDelta, lastDrift, -ent.sidewaysSpeed.toDouble() * driftCapMultiplier)
                } else 0.0
                lastDrift = drift
                if (isLogicalSideForUpdatingMovement) {
                    movementSpeed = getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED).toFloat()
                    super.travel(
                            Vec3d(
                                    drift,
                                    0.0,
                                    lerpedSpeed - (drift / 4)
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
            val height = y + mountedHeightOffset + passenger.heightOffset - 0.1
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
            attributes.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.27)
            attributes.add(EntityAttributes.GENERIC_MAX_HEALTH, 1.0)
            return attributes
        }

        const val forwardsTurnSpeed = 4.5f
        const val backwardsTurnSpeed = 1.5f
        const val driftDelta = 0.17
        const val driftCapMultiplier = 2
        const val speedDelta = 0.12
    }
}