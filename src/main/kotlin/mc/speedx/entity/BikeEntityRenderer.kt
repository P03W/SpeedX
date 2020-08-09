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