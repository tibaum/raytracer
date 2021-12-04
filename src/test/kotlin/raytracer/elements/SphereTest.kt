package raytracer.elements

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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

}
