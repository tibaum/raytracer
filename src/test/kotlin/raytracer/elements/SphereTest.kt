package raytracer.elements

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.PI
import kotlin.math.sqrt

class SphereTest {

    @Test
    fun testDefaultTransformation() {
        val sphere = Sphere()
        assertEquals(Matrix.identity(4), sphere.transformationMatrix)
    }

    @Test
    fun testChangingTranformation() {
        val translation = Matrix.translation(2.0, 3.0, 4.0)
        val sphere = Sphere(translation)
        assertEquals(translation, sphere.transformationMatrix)
    }

    @Test
    fun testNormalOnXAxis() {
        val sphere = Sphere()
        val normal = sphere.normalAt(Tuple.point(1.0, 0.0, 0.0))
        assertEquals(Tuple.vector(1.0, 0.0, 0.0), normal)
    }

    @Test
    fun testNormalOnYAxis() {
        val sphere = Sphere()
        val normal = sphere.normalAt(Tuple.point(0.0, 1.0, 0.0))
        assertEquals(Tuple.vector(0.0, 1.0, 0.0), normal)
    }

    @Test
    fun testNormalOnZAxis() {
        val sphere = Sphere()
        val normal = sphere.normalAt(Tuple.point(0.0, 0.0, 1.0))
        assertEquals(Tuple.vector(0.0, 0.0, 1.0), normal)
    }

    @Test
    fun testNormalAtNonAxialPoint() {
        val sphere = Sphere()
        val d = sqrt(3.0) / 3.0
        val normal = sphere.normalAt(Tuple.point(d, d, d))
        assertEquals(Tuple.vector(d, d, d), normal)
    }

    @Test
    fun testNormalIsANormalizedVector() {
        val sphere = Sphere()
        val d = sqrt(3.0) / 3.0
        val normal = sphere.normalAt(Tuple.point(d, d, d))
        assertEquals(normal, normal.normalize())
    }

    @Test
    fun testNormalOnTranslatedSphere() {
        val sphere = Sphere(Matrix.translation(0.0, 1.0, 0.0))
        val normal = sphere.normalAt(Tuple.point(0.0, 1.70711, -0.70711))
        assertEquals(Tuple.vector(0.0, 0.70711, -0.70711), normal)
    }

    @Test
    fun testNormalOnScaledSphere() {
        val sphere = Sphere(Matrix.scaling(1.0, 0.5, 1.0) * Matrix.rotationZ(PI / 5.0))
        val normal = sphere.normalAt(Tuple.point(0.0, sqrt(2.0) / 2.0, -sqrt(2.0) / 2.0))
        assertEquals(Tuple.vector(0.0, 0.97014, -0.24254), normal)
    }

}
