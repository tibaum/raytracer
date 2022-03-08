package raytracer.elements

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.PI
import kotlin.math.sqrt

class ShapeTest {

    @Test
    fun testDefaultShape() {
        val shape = object : Shape() {
            override fun localIntersect(ray: Ray): Intersections {
                return Intersections()
            }

            override fun localNormalAt(point: Tuple) = Tuple.vector(point[0], point[1], point[2])
        }

        assertEquals(Matrix.identity(4), shape.transformationMatrix)
        assertEquals(
            Material(
                surfaceColor = Tuple.color(1.0, 1.0, 1.0),
                ambientReflection = 0.1,
                diffuseReflection = 0.9,
                specularReflection = 0.9,
                shininess = 200.0
            ), shape.material
        )
    }

    @Test
    fun testIntersectingAScaledShapeWithRay() {
        val shape = createShape(Matrix.scaling(2.0, 2.0, 2.0))
        val ray = Ray(Tuple.point(0.0, 0.0, -5.0), Tuple.vector(0.0, 0.0, 1.0))
        shape.intersect(ray)
        assertEquals(Ray(Tuple.point(0.0, 0.0, -2.5), Tuple.vector(0.0, 0.0, 0.5)), shape.localRay)
    }

    @Test
    fun testIntersectingATranslatedShapeWithRay() {
        val shape = createShape(Matrix.translation(5.0, 0.0, 0.0))
        val ray = Ray(Tuple.point(0.0, 0.0, -5.0), Tuple.vector(0.0, 0.0, 1.0))
        shape.intersect(ray)
        assertEquals(Ray(Tuple.point(-5.0, 0.0, -5.0), Tuple.vector(0.0, 0.0, 1.0)), shape.localRay)
    }

    @Test
    fun testComputingNormalOnATranslatedShape() {
        val shape = createShape(Matrix.translation(0.0, 1.0, 0.0))
        val normal = shape.normalAt(Tuple.point(0.0, 1.70711, -0.70711))
        assertEquals(Tuple.vector(0.0, 0.70711, -0.70711), normal)
    }

    @Test
    fun testComputingNormalOnATransformedShape() {
        val shape = createShape(Matrix.scaling(1.0, 0.5, 1.0) * Matrix.rotationZ(PI / 5))
        val normal = shape.normalAt(Tuple.point(0.0, sqrt(2.0) / 2, -sqrt(2.0) / 2))
        assertEquals(Tuple.vector(0.0, 0.97014, -0.24254), normal)
    }

    @Test
    fun testNormalIsIsANormalizedVector() {
        val shape = createShape(Matrix.identity(4))
        val d = sqrt(3.0) / 3.0
        val normal = shape.normalAt(Tuple.point(d, d, d))
        assertEquals(normal, normal.normalize())
    }

    @Test
    fun testTranslatingRay() {
        val shape = createShape(Matrix.translation(3.0, 4.0, 5.0).inverse())
        val ray = Ray(Tuple.point(1.0, 2.0, 3.0), Tuple.vector(0.0, 1.0, 0.0))
        val rayTransformed: Ray = shape.relateRayToUnitShape(ray)
        assertEquals(Tuple.point(4.0, 6.0, 8.0), rayTransformed.origin)
        assertEquals(Tuple.vector(0.0, 1.0, 0.0), rayTransformed.direction)
    }

    @Test
    fun testScalingRay() {
        val shape = createShape(Matrix.scaling(2.0, 3.0, 4.0).inverse())
        val ray = Ray(Tuple.point(1.0, 2.0, 3.0), Tuple.vector(0.0, 1.0, 0.0))
        val rayTransformed = shape.relateRayToUnitShape(ray)
        assertEquals(Tuple.point(2.0, 6.0, 12.0), rayTransformed.origin)
        assertEquals(Tuple.vector(0.0, 3.0, 0.0), rayTransformed.direction)
    }

    @Test
    fun testLightningWithEyeBetweenLightAndSurface() {
        val shape = createShape(Matrix.identity(4))
        val position = Tuple.point(0.0, 0.0, 0.0)
        val eyeVector = Tuple.vector(0.0, 0.0, -1.0)
        val normalVector = Tuple.vector(0.0, 0.0, -1.0)
        val pointLight = PointLight(
            position = Tuple.point(0.0, 0.0, -10.0),
            intensity = Tuple.color(1.0, 1.0, 1.0)
        )
        val color = shape.lightning(pointLight, position, eyeVector, normalVector, false)
        assertEquals(Tuple(1.9, 1.9, 1.9), color)
    }

    @Test
    fun testLightningWithEyeBetweenLightAndSurfaceEyeOffset45Degrees() {
        val shape = createShape(Matrix.identity(4))
        val position = Tuple.point(0.0, 0.0, 0.0)
        val eyeVector = Tuple.vector(0.0, sqrt(2.0) / 2.0, -sqrt(2.0) / 2.0)
        val normalVector = Tuple.vector(0.0, 0.0, -1.0)
        val light = PointLight(
            position = Tuple.point(0.0, 0.0, -10.0),
            intensity = Tuple.color(1.0, 1.0, 1.0)
        )
        val color = shape.lightning(light, position, eyeVector, normalVector, false)
        assertEquals(Tuple.color(1.0, 1.0, 1.0), color)
    }

    @Test
    fun testLightningWithEyeInPathOfReflectionVector() {
        val shape = createShape(Matrix.identity(4))
        val position = Tuple.point(0.0, 0.0, 0.0)
        val eyeVector = Tuple.vector(0.0, -sqrt(2.0) / 2.0, -sqrt(2.0) / 2.0)
        val normalVector = Tuple.vector(0.0, 0.0, -1.0)
        val light = PointLight(
            position = Tuple.point(0.0, 10.0, -10.0),
            intensity = Tuple.color(1.0, 1.0, 1.0)
        )
        val color = shape.lightning(light, position, eyeVector, normalVector, false)
        assertEquals(Tuple(1.6364, 1.6364, 1.6364), color)
    }

    @Test
    fun testLightningWithEyeOppositeSurfaceLightOffset45Degrees() {
        val shape = createShape(Matrix.identity(4))
        val position = Tuple.point(0.0, 0.0, 0.0)
        val eyeVector = Tuple.vector(0.0, 0.0, -1.0)
        val normalVector = Tuple.vector(0.0, 0.0, -1.0)
        val light = PointLight(
            position = Tuple.point(0.0, 10.0, -10.0),
            intensity = Tuple.color(1.0, 1.0, 1.0)
        )
        val color = shape.lightning(light, position, eyeVector, normalVector, false)
        assertEquals(Tuple(0.7364, 0.7364, 0.7364), color)
    }

    @Test
    fun testLightningWithLightBehindSurface() {
        val shape = createShape(Matrix.identity(4))
        val position = Tuple.point(0.0, 0.0, 0.0)
        val eyeVector = Tuple.vector(0.0, 0.0, -1.0)
        val normalVector = Tuple.vector(0.0, 0.0, -1.0)
        val light = PointLight(
            position = Tuple.point(0.0, 0.0, 10.0),
            intensity = Tuple.color(1.0, 1.0, 1.0)
        )
        val color = shape.lightning(light, position, eyeVector, normalVector, false)
        assertEquals(Tuple(0.1, 0.1, 0.1), color)
    }

    @Test
    fun testLightningWithSurfaceInShadow() {
        val shape = createShape(Matrix.identity(4))
        val position = Tuple.point(0.0, 0.0, 0.0)
        val eyeVector = Tuple.vector(0.0, 0.0, -1.0)
        val normalVector = Tuple.vector(0.0, 0.0, -1.0)
        val pointLight = PointLight(
            position = Tuple.point(0.0, 0.0, -10.0),
            intensity = Tuple.color(1.0, 1.0, 1.0)
        )
        val color = shape.lightning(pointLight, position, eyeVector, normalVector, true)
        assertEquals(Tuple(0.1, 0.1, 0.1), color)
    }

    private fun createShape(transformationMatrix: Matrix) =
        object : Shape(transformationMatrix) {
            lateinit var localRay: Ray
            override fun localIntersect(ray: Ray): Intersections {
                localRay = ray
                return Intersections()
            }

            override fun localNormalAt(point: Tuple) = Tuple.vector(point[0], point[1], point[2])
        }

}
