package mc.speedx.main.mixin;

import mc.speedx.entity.BikeEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> extends AnimalModel<T> {
    @Shadow
    public ModelPart rightLeg;
    @Shadow
    public ModelPart leftLeg;

    @Shadow public float leaningPitch;

    @Shadow public ModelPart torso;

    @Shadow public ModelPart head;

    @Shadow public ModelPart leftArm;

    @Shadow public ModelPart rightArm;

    @Inject(at = @At("TAIL"), method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V")
    public void setAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
        if (this.riding && livingEntity.getVehicle() instanceof BikeEntity) {
            this.torso.pitch = 0.5F;
            this.torso.pivotZ = -5F;
            this.torso.pivotY = 2.5F;

            this.head.pivotZ = -5.7F;
            this.head.pivotY = 2.7F;

            this.leftArm.pivotZ = -5.2F;
            this.leftArm.pivotY = 4.2F;

            this.rightArm.pivotZ = -5.2F;
            this.rightArm.pivotY = 4.2F;

            this.rightLeg.pitch = -0.4F;
            this.rightLeg.yaw = 0.1F;
            this.rightLeg.roll = 0.1F;

            this.leftLeg.pitch = -0.4F;
            this.leftLeg.yaw = -0.1F;
            this.leftLeg.roll = -0.1F;
        }
    }
}
