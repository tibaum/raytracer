package raytracer.elements

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import raytracer.elements.Tuple.Companion.black
import raytracer.elements.Tuple.Companion.color
import raytracer.elements.Tuple.Companion.point
import raytracer.elements.Tuple.Companion.vector
import raytracer.elements.Tuple.Companion.white
import kotlin.math.PI
import kotlin.math.sqrt

internal class ShapeTest {

    @Test
    fun testIntersectingAScaledShapeWithRay() {
        val shape = createShape(Matrix.scaling(2.0, 2.0, 2.0))
        val ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        shape.intersect(ray)
        assertEquals(Ray(point(0.0, 0.0, -2.5), vector(0.0, 0.0, 0.5)), shape.localRay)
    }

    @Test
    fun testIntersectingATranslatedShapeWithRay() {
        val shape = createShape(Matrix.translation(5.0, 0.0, 0.0))
        val ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        shape.intersect(ray)
        assertEquals(Ray(point(-5.0, 0.0, -5.0), vector(0.0, 0.0, 1.0)), shape.localRay)
    }

    @Test
    fun testComputingNormalOnATranslatedShape() {
        val shape = createShape(Matrix.translation(0.0, 1.0, 0.0))
        val normal = shape.normalAt(point(0.0, 1.70711, -0.70711))
        assertEquals(vector(0.0, 0.70711, -0.70711), normal)
    }

    @Test
    fun testComputingNormalOnATransformedShape() {
        val shape = createShape(Matrix.scaling(1.0, 0.5, 1.0) * Matrix.rotationZ(PI / 5))
        val normal = shape.normalAt(point(0.0, sqrt(2.0) / 2, -sqrt(2.0) / 2))
        assertEquals(vector(0.0, 0.97014, -0.24254), normal)
    }

    @Test
    fun testNormalIsIsANormalizedVector() {
        val shape = createShape()
        val d = sqrt(3.0) / 3.0
        val normal = shape.normalAt(point(d, d, d))
        assertEquals(normal, normal.normalize())
    }

    @Test
    fun testTranslatingRay() {
        val shape = createShape(Matrix.translation(3.0, 4.0, 5.0).inverse())
        val ray = Ray(point(1.0, 2.0, 3.0), vector(0.0, 1.0, 0.0))
        val rayTransformed: Ray = shape.relateRayToUnitShape(ray)
        assertEquals(point(4.0, 6.0, 8.0), rayTransformed.origin)
        assertEquals(vector(0.0, 1.0, 0.0), rayTransformed.direction)
    }

    @Test
    fun testScalingRay() {
        val shape = createShape(Matrix.scaling(2.0, 3.0, 4.0).inverse())
        val ray = Ray(point(1.0, 2.0, 3.0), vector(0.0, 1.0, 0.0))
        val rayTransformed = shape.relateRayToUnitShape(ray)
        assertEquals(point(2.0, 6.0, 12.0), rayTransformed.origin)
        assertEquals(vector(0.0, 3.0, 0.0), rayTransformed.direction)
    }

    @Test
    fun testLightningWithEyeBetweenLightAndSurface() {
        val shape = createShape()
        val position = point(0.0, 0.0, 0.0)
        val eyeVector = vector(0.0, 0.0, -1.0)
        val normalVector = vector(0.0, 0.0, -1.0)
        val pointLight = PointLight(
            position = point(0.0, 0.0, -10.0),
            intensity = color(1.0, 1.0, 1.0)
        )
        val color = shape.lightning(pointLight, position, eyeVector, normalVector, false)
        assertEquals(Tuple(1.9, 1.9, 1.9), color)
    }

    @Test
    fun testLightningWithEyeBetweenLightAndSurfaceEyeOffset45Degrees() {
        val shape = createShape()
        val position = point(0.0, 0.0, 0.0)
        val eyeVector = vector(0.0, sqrt(2.0) / 2.0, -sqrt(2.0) / 2.0)
        val normalVector = vector(0.0, 0.0, -1.0)
        val light = PointLight(
            position = point(0.0, 0.0, -10.0),
            intensity = color(1.0, 1.0, 1.0)
        )
        val color = shape.lightning(light, position, eyeVector, normalVector, false)
        assertEquals(color(1.0, 1.0, 1.0), color)
    }

    @Test
    fun testLightningWithEyeInPathOfReflectionVector() {
        val shape = createShape()
        val position = point(0.0, 0.0, 0.0)
        val eyeVector = vector(0.0, -sqrt(2.0) / 2.0, -sqrt(2.0) / 2.0)
        val normalVector = vector(0.0, 0.0, -1.0)
        val light = PointLight(
            position = point(0.0, 10.0, -10.0),
            intensity = color(1.0, 1.0, 1.0)
        )
        val color = shape.lightning(light, position, eyeVector, normalVector, false)
        assertEquals(Tuple(1.6364, 1.6364, 1.6364), color)
    }

    @Test
    fun testLightningWithEyeOppositeSurfaceLightOffset45Degrees() {
        val shape = createShape()
        val position = point(0.0, 0.0, 0.0)
        val eyeVector = vector(0.0, 0.0, -1.0)
        val normalVector = vector(0.0, 0.0, -1.0)
        val light = PointLight(
            position = point(0.0, 10.0, -10.0),
            intensity = color(1.0, 1.0, 1.0)
        )
        val color = shape.lightning(light, position, eyeVector, normalVector, false)
        assertEquals(Tuple(0.7364, 0.7364, 0.7364), color)
    }

    @Test
    fun testLightningWithLightBehindSurface() {
        val shape = createShape()
        val position = point(0.0, 0.0, 0.0)
        val eyeVector = vector(0.0, 0.0, -1.0)
        val normalVector = vector(0.0, 0.0, -1.0)
        val light = PointLight(
            position = point(0.0, 0.0, 10.0),
            intensity = color(1.0, 1.0, 1.0)
        )
        val color = shape.lightning(light, position, eyeVector, normalVector, false)
        assertEquals(Tuple(0.1, 0.1, 0.1), color)
    }

    @Test
    fun testLightningWithSurfaceInShadow() {
        val shape = createShape()
        val position = point(0.0, 0.0, 0.0)
        val eyeVector = vector(0.0, 0.0, -1.0)
        val normalVector = vector(0.0, 0.0, -1.0)
        val pointLight = PointLight(
            position = point(0.0, 0.0, -10.0),
            intensity = color(1.0, 1.0, 1.0)
        )
        val color = shape.lightning(pointLight, position, eyeVector, normalVector, true)
        assertEquals(Tuple(0.1, 0.1, 0.1), color)
    }

    @Test
    fun testLightningWithPatternApplied() {
        val shape = createShape(
            material = Material(
                surfaceColor = color(0.5, 0.5, 0.5),
                ambientReflection = 1.0,
                diffuseReflection = 0.0,
                specularReflection = 0.0,
                shininess = 200.0,
                pattern = StripePattern()
            )
        )
        val eyeVector = vector(0.0, 0.0, -1.0)
        val normalVector = vector(0.0, 0.0, -1.0)
        val pointLight = PointLight(
            position = point(0.0, 0.0, -10.0),
            intensity = color(1.0, 1.0, 1.0)
        )
        val color1 = shape.lightning(pointLight, point(0.9, 0.0, 0.0), eyeVector, normalVector, false)
        val color2 = shape.lightning(pointLight, point(1.1, 0.0, 0.0), eyeVector, normalVector, false)
        assertEquals(white, color1)
        assertEquals(black, color2)
    }

    private fun createShape(
        transformationMatrix: Matrix = Matrix.identity(4),
        material: Material = Material(pattern = StripePattern())
    ) = object : Shape(transformationMatrix, material) {
        lateinit var localRay: Ray
        override fun localIntersect(ray: Ray): Intersections {
            localRay = ray
            return Intersections()
        }

        override fun localNormalAt(point: Tuple) = vector(point[0], point[1], point[2])
    }

}
