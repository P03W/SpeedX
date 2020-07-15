package mc.speedx.main

import mc.speedx.entity.BikeEntityRenderer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.EntityRenderDispatcher


class SpeedXClient : ClientModInitializer {
    override fun onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(SpeedX.BIKE) { dispatcher: EntityRenderDispatcher, context: EntityRendererRegistry.Context? -> BikeEntityRenderer(dispatcher) }
    }
}