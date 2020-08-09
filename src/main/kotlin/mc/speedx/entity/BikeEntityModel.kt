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

import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Vec3d


class BikeEntityModel : EntityModel<BikeEntity>() {
    private val pedals: ModelPart
    private val frame: ModelPart
    private val seat: ModelPart
    private val seatAttach: ModelPart
    private val backAttach: ModelPart
    private val frontAttach: ModelPart
    private val pedalsAttachBack: ModelPart
    private val pedalsAttachFront: ModelPart
    private val handles: ModelPart
    private val right: ModelPart
    private val left: ModelPart
    private val handleAttach: ModelPart
    private val wheelF: ModelPart
    private val rotate: ModelPart
    private val rotate2: ModelPart
    private val rotate3: ModelPart
    private val spoke: ModelPart
    private val spoke2: ModelPart
    private val spoke3: ModelPart
    private val spoke4: ModelPart
    private val wheelB: ModelPart
    private val rotate4: ModelPart
    private val rotate5: ModelPart
    private val rotate6: ModelPart
    private val spoke5: ModelPart
    private val spoke6: ModelPart
    private val spoke7: ModelPart
    private val spoke8: ModelPart
    override fun setAngles(entity: BikeEntity, limbAngle: Float, limbDistance: Float, animationProgress: Float, headYaw: Float, headPitch: Float) {
        val speed: Double = entity.velocity.dotProduct(Vec3d(0.0, 0.0, 1.0).rotateY(Math.toRadians((-entity.bodyYaw).toDouble()).toFloat()))
        val radius = 0.25
        val circumference = 2 * Math.PI * radius
        entity.wheelPosition += (speed / circumference).toFloat()
        entity.wheelPosition = entity.wheelPosition % 360f
        wheelB.pitch = entity.wheelPosition
        wheelF.pitch = entity.wheelPosition
        pedals.pitch = (entity.wheelPosition + 180f) % 360f
    }

    private fun setRotationAngle(modelRenderer: ModelPart, x: Float, y: Float, z: Float) {
        modelRenderer.pitch = x
        modelRenderer.yaw = y
        modelRenderer.roll = z
    }

    override fun render(matrices: MatrixStack, vertices: VertexConsumer, light: Int, overlay: Int, red: Float, green: Float, blue: Float, alpha: Float) {
        matrices.push()
        matrices.translate(0.0, -0.45, 0.0)
        matrices.scale(1.3f, 1.3f, 1.3f)
        wheelB.render(matrices, vertices, light, overlay, red, green, blue, alpha)
        wheelF.render(matrices, vertices, light, overlay, red, green, blue, alpha)
        frame.render(matrices, vertices, light, overlay, red, green, blue, alpha)
        handles.render(matrices, vertices, light, overlay, red, green, blue, alpha)
        pedals.render(matrices, vertices, light, overlay, red, green, blue, alpha)
        matrices.pop()
    }

    init {
        textureWidth = 32
        textureHeight = 32
        pedals = ModelPart(this)
        pedals.setPivot(0.0f, 19.5f, 2.0f)
        pedals.setTextureOffset(12, 27).addCuboid(0.5f, -0.5f, 0.5f, 2.0f, 1.0f, 2.0f, 0.0f, false)
        pedals.setTextureOffset(12, 27).addCuboid(-2.5f, -0.5f, -2.5f, 2.0f, 1.0f, 2.0f, 0.0f, false)
        // Tweaked width to fix Z-fighting
        pedals.setTextureOffset(12, 23).addCuboid(-0.505f, -1.0f, -1.0f, 1.01f, 2.0f, 2.0f, 0.0f, false)
        frame = ModelPart(this)
        frame.setPivot(0.5f, 19.0f, 0.0f)
        setRotationAngle(frame, -0.0873f, 0.0f, 0.0f)
        frame.setTextureOffset(0, 12).addCuboid(-1.0f, -5.981f, -5.4358f, 1.0f, 1.0f, 10.0f, 0.0f, false)
        seat = ModelPart(this)
        seat.setPivot(-0.625f, 2.0757f, 2.4924f)
        frame.addChild(seat)
        setRotationAngle(seat, -0.0873f, 0.0f, 0.0f)
        seat.setTextureOffset(16, 27).addCuboid(-1.125f, -11.0863f, -0.2637f, 2.0f, 1.0f, 4.0f, 0.0f, false)
        seatAttach = ModelPart(this)
        seatAttach.setPivot(0.125f, -3.25f, 2.25f)
        seat.addChild(seatAttach)
        setRotationAngle(seatAttach, -0.0873f, 0.0f, 0.0f)
        seatAttach.setTextureOffset(0, 17).addCuboid(-0.5f, -7.322f, -1.8809f, 1.0f, 3.0f, 1.0f, 0.0f, false)
        backAttach = ModelPart(this)
        backAttach.setPivot(-1.5f, -0.1139f, 3.5605f)
        frame.addChild(backAttach)
        setRotationAngle(backAttach, 0.8727f, 0.0f, 0.0f)
        backAttach.setTextureOffset(8, 23).addCuboid(-0.5f, -3.1464f, 3.5f, 1.0f, 8.0f, 1.0f, 0.0f, false)
        frontAttach = ModelPart(this)
        frontAttach.setPivot(0.0f, 1.7433f, 0.1525f)
        frame.addChild(frontAttach)
        setRotationAngle(frontAttach, -0.3491f, 0.0f, 0.0f)
        frontAttach.setTextureOffset(0, 23).addCuboid(0.0f, -5.2156f, -8.9658f, 1.0f, 8.0f, 1.0f, 0.0f, false)
        frontAttach.setTextureOffset(0, 23).addCuboid(-2.0f, -5.2156f, -8.9658f, 1.0f, 8.0f, 1.0f, 0.0f, false)
        frontAttach.setTextureOffset(4, 23).addCuboid(-1.0f, 1.7763f, -8.9116f, 1.0f, 1.0f, 1.0f, 0.0f, false)
        pedalsAttachBack = ModelPart(this)
        pedalsAttachBack.setPivot(-0.5f, -2.7359f, 3.1524f)
        frame.addChild(pedalsAttachBack)
        setRotationAngle(pedalsAttachBack, -0.2618f, 0.0f, 0.0f)
        pedalsAttachBack.setTextureOffset(4, 26).addCuboid(-0.5f, -2.5f, -0.5f, 1.0f, 5.0f, 1.0f, 0.0f, false)
        pedalsAttachFront = ModelPart(this)
        pedalsAttachFront.setPivot(-0.5f, -1.8731f, -0.9071f)
        frame.addChild(pedalsAttachFront)
        setRotationAngle(pedalsAttachFront, 0.9599f, 0.0f, 0.0f)
        pedalsAttachFront.setTextureOffset(28, 22).addCuboid(-0.5f, -6.0f, -0.5f, 1.0f, 9.0f, 1.0f, 0.0f, false)
        handles = ModelPart(this)
        handles.setPivot(0.0f, 13.5f, -6.5f)
        right = ModelPart(this)
        right.setPivot(0.0f, 0.0f, 0.0f)
        handles.addChild(right)
        setRotationAngle(right, 0.0f, 0.2618f, 0.0f)
        right.setTextureOffset(12, 15).addCuboid(-4.0126f, -5.0f, 1.0093f, 3.0f, 1.0f, 1.0f, 0.0f, false)
        left = ModelPart(this)
        left.setPivot(0.0f, 0.0f, 0.0f)
        handles.addChild(left)
        setRotationAngle(left, 0.0f, -0.2618f, 0.0f)
        left.setTextureOffset(12, 13).addCuboid(1.005f, -5.0f, 1.0093f, 3.0f, 1.0f, 1.0f, 0.0f, false)
        handleAttach = ModelPart(this)
        handleAttach.setPivot(0.0f, 1.0f, 1.0f)
        handles.addChild(handleAttach)
        setRotationAngle(handleAttach, -0.1745f, 0.0f, 0.0f)
        handleAttach.setTextureOffset(12, 17).addCuboid(-0.5f, -5.7352f, -0.7902f, 1.0f, 4.0f, 1.0f, 0.0f, false)
        wheelF = ModelPart(this)
        wheelF.setPivot(0.0f, 19.5f, -8.5f)
        wheelF.setTextureOffset(2, 19).addCuboid(-0.5f, 3.5f, -1.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        wheelF.setTextureOffset(1, 13).addCuboid(-0.5f, -4.5f, -1.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        wheelF.setTextureOffset(0, 12).addCuboid(-0.5f, -1.25f, -4.5f, 1.0f, 2.0f, 1.0f, 0.0f, false)
        wheelF.setTextureOffset(0, 12).addCuboid(-0.5f, -1.0f, 3.5f, 1.0f, 2.0f, 1.0f, 0.0f, false)
        rotate = ModelPart(this)
        rotate.setPivot(0.0f, -0.0625f, 0.0f)
        wheelF.addChild(rotate)
        setRotationAngle(rotate, 0.3927f, 0.0f, 0.0f)
        rotate.setTextureOffset(0, 12).addCuboid(-0.5f, -0.9375f, 3.5f, 1.0f, 2.0f, 1.0f, 0.0f, false)
        rotate.setTextureOffset(2, 19).addCuboid(-0.5f, 3.5625f, -1.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        rotate.setTextureOffset(1, 13).addCuboid(-0.5f, -4.4375f, -1.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        rotate.setTextureOffset(0, 12).addCuboid(-0.5f, -1.1875f, -4.5f, 1.0f, 2.0f, 1.0f, 0.0f, false)
        rotate2 = ModelPart(this)
        rotate2.setPivot(0.0f, -0.0625f, 0.0f)
        wheelF.addChild(rotate2)
        setRotationAngle(rotate2, 0.7854f, 0.0f, 0.0f)
        rotate2.setTextureOffset(0, 12).addCuboid(-0.5f, -0.9375f, 3.5f, 1.0f, 2.0f, 1.0f, 0.0f, false)
        rotate2.setTextureOffset(2, 19).addCuboid(-0.5f, 3.5625f, -1.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        rotate2.setTextureOffset(1, 13).addCuboid(-0.5f, -4.4375f, -1.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        rotate2.setTextureOffset(0, 12).addCuboid(-0.5f, -1.1875f, -4.5f, 1.0f, 2.0f, 1.0f, 0.0f, false)
        rotate3 = ModelPart(this)
        rotate3.setPivot(0.0f, -0.0625f, 0.0f)
        wheelF.addChild(rotate3)
        setRotationAngle(rotate3, 1.1781f, 0.0f, 0.0f)
        rotate3.setTextureOffset(0, 12).addCuboid(-0.5f, -0.9375f, 3.5f, 1.0f, 2.0f, 1.0f, 0.0f, false)
        rotate3.setTextureOffset(2, 19).addCuboid(-0.5f, 3.5625f, -1.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        rotate3.setTextureOffset(1, 13).addCuboid(-0.5f, -4.4375f, -1.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        rotate3.setTextureOffset(0, 12).addCuboid(-0.5f, -1.1875f, -4.5f, 1.0f, 2.0f, 1.0f, 0.0f, false)
        spoke = ModelPart(this)
        spoke.setPivot(0.0f, 0.0f, 0.0f)
        wheelF.addChild(spoke)
        setRotationAngle(spoke, -0.7854f, 0.0f, 0.0f)
        spoke.setTextureOffset(23, 14).addCuboid(0.0f, -3.5f, -0.25f, 0.0f, 7.0f, 0.5f, 0.0f, false)
        spoke2 = ModelPart(this)
        spoke2.setPivot(0.0f, 0.0f, 0.0f)
        wheelF.addChild(spoke2)
        setRotationAngle(spoke2, 0.7854f, 0.0f, 0.0f)
        spoke2.setTextureOffset(23, 14).addCuboid(0.0f, -3.5f, -0.25f, 0.0f, 7.0f, 0.5f, 0.0f, false)
        spoke3 = ModelPart(this)
        spoke3.setPivot(0.0f, 0.0f, 0.0f)
        wheelF.addChild(spoke3)
        setRotationAngle(spoke3, 1.5708f, 0.0f, 0.0f)
        spoke3.setTextureOffset(23, 14).addCuboid(0.0f, -3.5f, -0.25f, 0.0f, 7.0f, 0.5f, 0.0f, false)
        spoke4 = ModelPart(this)
        spoke4.setPivot(0.0f, 0.0f, 0.0f)
        wheelF.addChild(spoke4)
        setRotationAngle(spoke4, 3.1416f, 0.0f, 0.0f)
        spoke4.setTextureOffset(23, 14).addCuboid(0.0f, -3.5f, -0.25f, 0.0f, 7.0f, 0.5f, 0.0f, false)
        wheelB = ModelPart(this)
        wheelB.setPivot(0.0f, 19.5f, 9.5f)
        wheelB.setTextureOffset(2, 19).addCuboid(-0.5f, 3.5f, -1.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        wheelB.setTextureOffset(1, 13).addCuboid(-0.5f, -4.5f, -1.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        wheelB.setTextureOffset(0, 12).addCuboid(-0.5f, -1.25f, -4.5f, 1.0f, 2.0f, 1.0f, 0.0f, false)
        wheelB.setTextureOffset(0, 12).addCuboid(-0.5f, -1.0f, 3.5f, 1.0f, 2.0f, 1.0f, 0.0f, false)
        rotate4 = ModelPart(this)
        rotate4.setPivot(0.0f, -0.0625f, 0.0f)
        wheelB.addChild(rotate4)
        setRotationAngle(rotate4, 0.3927f, 0.0f, 0.0f)
        rotate4.setTextureOffset(0, 12).addCuboid(-0.5f, -0.9375f, 3.5f, 1.0f, 2.0f, 1.0f, 0.0f, false)
        rotate4.setTextureOffset(2, 19).addCuboid(-0.5f, 3.5625f, -1.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        rotate4.setTextureOffset(1, 13).addCuboid(-0.5f, -4.4375f, -1.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        rotate4.setTextureOffset(0, 12).addCuboid(-0.5f, -1.1875f, -4.5f, 1.0f, 2.0f, 1.0f, 0.0f, false)
        rotate5 = ModelPart(this)
        rotate5.setPivot(0.0f, -0.0625f, 0.0f)
        wheelB.addChild(rotate5)
        setRotationAngle(rotate5, 0.7854f, 0.0f, 0.0f)
        rotate5.setTextureOffset(0, 12).addCuboid(-0.5f, -0.9375f, 3.5f, 1.0f, 2.0f, 1.0f, 0.0f, false)
        rotate5.setTextureOffset(2, 19).addCuboid(-0.5f, 3.5625f, -1.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        rotate5.setTextureOffset(1, 13).addCuboid(-0.5f, -4.4375f, -1.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        rotate5.setTextureOffset(0, 12).addCuboid(-0.5f, -1.1875f, -4.5f, 1.0f, 2.0f, 1.0f, 0.0f, false)
        rotate6 = ModelPart(this)
        rotate6.setPivot(0.0f, -0.0625f, 0.0f)
        wheelB.addChild(rotate6)
        setRotationAngle(rotate6, 1.1781f, 0.0f, 0.0f)
        rotate6.setTextureOffset(0, 12).addCuboid(-0.5f, -0.9375f, 3.5f, 1.0f, 2.0f, 1.0f, 0.0f, false)
        rotate6.setTextureOffset(2, 19).addCuboid(-0.5f, 3.5625f, -1.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        rotate6.setTextureOffset(1, 13).addCuboid(-0.5f, -4.4375f, -1.0f, 1.0f, 1.0f, 2.0f, 0.0f, false)
        rotate6.setTextureOffset(0, 12).addCuboid(-0.5f, -1.1875f, -4.5f, 1.0f, 2.0f, 1.0f, 0.0f, false)
        spoke5 = ModelPart(this)
        spoke5.setPivot(0.0f, 0.0f, 0.0f)
        wheelB.addChild(spoke5)
        setRotationAngle(spoke5, -0.7854f, 0.0f, 0.0f)
        spoke5.setTextureOffset(23, 14).addCuboid(0.0f, -3.5f, -0.25f, 0.0f, 7.0f, 0.5f, 0.0f, false)
        spoke6 = ModelPart(this)
        spoke6.setPivot(0.0f, 0.0f, 0.0f)
        wheelB.addChild(spoke6)
        setRotationAngle(spoke6, 0.7854f, 0.0f, 0.0f)
        spoke6.setTextureOffset(23, 14).addCuboid(0.0f, -3.5f, -0.25f, 0.0f, 7.0f, 0.5f, 0.0f, false)
        spoke7 = ModelPart(this)
        spoke7.setPivot(0.0f, 0.0f, 0.0f)
        wheelB.addChild(spoke7)
        setRotationAngle(spoke7, 1.5708f, 0.0f, 0.0f)
        spoke7.setTextureOffset(23, 14).addCuboid(0.0f, -3.5f, -0.25f, 0.0f, 7.0f, 0.5f, 0.0f, false)
        spoke8 = ModelPart(this)
        spoke8.setPivot(0.0f, 0.0f, 0.0f)
        wheelB.addChild(spoke8)
        setRotationAngle(spoke8, 3.1416f, 0.0f, 0.0f)
        spoke8.setTextureOffset(23, 14).addCuboid(0.0f, -3.5f, -0.25f, 0.0f, 7.0f, 0.5f, 0.0f, false)
    }
}
