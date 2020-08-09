/*
 * MIT License
 *
 * Copyright (c) 2020 TheoreticallyUseful
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
        ) { dispatcher: EntityRenderDispatcher?, _: EntityRendererRegistry.Context? ->
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

