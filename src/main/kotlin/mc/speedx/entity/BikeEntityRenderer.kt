package mc.speedx.entity

import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.util.Identifier

class BikeEntityRenderer : LivingEntityRenderer<BikeEntity, BikeEntityModel?> {
    constructor(dispatcher: EntityRenderDispatcher?) : super(dispatcher, BikeEntityModel(), 0.0f) {}
    constructor(dispatcher: EntityRenderDispatcher?, model: BikeEntityModel?, shadowRadius: Float) : super(dispatcher, model, shadowRadius) {}

    override fun getTexture(entity: BikeEntity): Identifier {
        return Identifier("speedx", "textures/entity/bike/bike.png")
    }

    override fun hasLabel(ent: BikeEntity): Boolean {
        // For some reason, this logic isn't overridden in LivingEntityRenderer, only in MobEntityRenderer
        return super.hasLabel(ent) && (ent.shouldRenderName() || ent.hasCustomName() && ent === dispatcher.targetedEntity)
    }
}