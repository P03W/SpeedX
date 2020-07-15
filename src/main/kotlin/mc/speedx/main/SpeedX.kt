package mc.speedx.main

import mc.speedx.entity.BikeEntity
import mc.speedx.entity.BikeEntityRenderer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry


class SpeedX : ModInitializer {
    override fun onInitialize() {
        EntityRendererRegistry.INSTANCE.register(
            BIKE
        ) { dispatcher: EntityRenderDispatcher?, context: EntityRendererRegistry.Context? ->
            BikeEntityRenderer(
                dispatcher!!
            )
        }

        FabricDefaultAttributeRegistry.register(BIKE, BikeEntity.createBikeAttributes())
    }

    companion object {
        val BIKE: EntityType<BikeEntity> = Registry.register(
            Registry.ENTITY_TYPE,
            Identifier("speedx", "bike"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::BikeEntity)
                    .dimensions(EntityDimensions.fixed(1f, 1.25f))
                    .trackable(128, 1, true)
                    .build()
        )
    }
}

