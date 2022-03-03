package raytracer.elements

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.PI
import kotlin.math.sqrt

class CameraTest {

    @Test
    fun testConstructRayThroughCenterOfCanvas() {
        val camera = Camera(201, 101, PI / 2)
        val ray = camera.rayForPixel(100, 50)
        assertEquals(Tuple.point(0.0, 0.0, 0.0), ray.origin)
        assertEquals(Tuple.vector(0.0, 0.0, -1.0), ray.direction)
    }

    @Test
    fun testConstructRayThroughCornerOfCanvas() {
        val camera = Camera(201, 101, PI / 2)
        val ray = camera.rayForPixel(0, 0)
        assertEquals(Tuple.point(0.0, 0.0, 0.0), ray.origin)
        assertEquals(Tuple.vector(0.66519, 0.33259, -0.66851), ray.direction)
    }

    @Test
    fun testConstructRayWhenCameraIsTransformed() {
        val transformationMatrix = Matrix.rotationY(PI / 4) * Matrix.translation(0.0, -2.0, 5.0)
        val camera = Camera(201, 101, PI / 2, transformationMatrix)
        val ray = camera.rayForPixel(100, 50)
        assertEquals(Tuple.point(0.0, 2.0, -5.0), ray.origin)
        assertEquals(Tuple.vector(sqrt(2.0) / 2, 0.0, -sqrt(2.0) / 2), ray.direction)
    }

    @Test
    fun testRenderWorld() {
        val transformationMatrix = Matrix.viewTransform(
            from = Tuple.point(0.0, 0.0, -5.0),
            to = Tuple.point(0.0, 0.0, 0.0),
            up = Tuple.vector(0.0, 1.0, 0.0)
        )
        val camera = Camera(11, 11, PI / 2, transformationMatrix)
        val image: Canvas = camera.render(World())
        assertEquals(Tuple.color(0.38066, 0.47583, 0.2855), image.pixelAt(5, 5))
    }

}
