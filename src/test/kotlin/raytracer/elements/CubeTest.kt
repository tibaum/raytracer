package raytracer.elements

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import raytracer.elements.Tuple.Companion.point
import raytracer.elements.Tuple.Companion.vector

internal class CubeTest {

    @Test
    fun testRayIntersectCubeFromPositiveX() {
        val cube = Cube()
        val ray = Ray(point(5.0, 0.5, 0.0), vector(-1.0, 0.0, 0.0))
        val intersections = cube.localIntersect(ray)
        assertEquals(2, intersections.count)
        assertEquals(4.0, intersections[0].time)
        assertEquals(6.0, intersections[1].time)
    }

    @Test
    fun testRayIntersectCubeFromNegativeX() {
        val cube = Cube()
        val ray = Ray(point(-5.0, 0.5, 0.0), vector(1.0, 0.0, 0.0))
        val intersections = cube.localIntersect(ray)
        assertEquals(2, intersections.count)
        assertEquals(4.0, intersections[0].time)
        assertEquals(6.0, intersections[1].time)
    }

    @Test
    fun testRayIntersectCubeFromPositiveY() {
        val cube = Cube()
        val ray = Ray(point(0.5, 5.0, 0.0), vector(0.0, -1.0, 0.0))
        val intersections = cube.localIntersect(ray)
        assertEquals(2, intersections.count)
        assertEquals(4.0, intersections[0].time)
        assertEquals(6.0, intersections[1].time)
    }

    @Test
    fun testRayIntersectCubeFromNegativeY() {
        val cube = Cube()
        val ray = Ray(point(0.5, -5.0, 0.0), vector(0.0, 1.0, 0.0))
        val intersections = cube.localIntersect(ray)
        assertEquals(2, intersections.count)
        assertEquals(4.0, intersections[0].time)
        assertEquals(6.0, intersections[1].time)
    }

    @Test
    fun testRayIntersectCubeFromPositiveZ() {
        val cube = Cube()
        val ray = Ray(point(0.5, 0.0, 5.0), vector(0.0, 0.0, -1.0))
        val intersections = cube.localIntersect(ray)
        assertEquals(2, intersections.count)
        assertEquals(4.0, intersections[0].time)
        assertEquals(6.0, intersections[1].time)
    }

    @Test
    fun testRayIntersectCubeFromNegativeZ() {
        val cube = Cube()
        val ray = Ray(point(0.5, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        val intersections = cube.localIntersect(ray)
        assertEquals(2, intersections.count)
        assertEquals(4.0, intersections[0].time)
        assertEquals(6.0, intersections[1].time)
    }

    @Test
    fun testRayIntersectCubeFromInside() {
        val cube = Cube()
        val ray = Ray(point(0.0, 0.5, 0.0), vector(0.0, 0.0, 1.0))
        val intersections = cube.localIntersect(ray)
        assertEquals(2, intersections.count)
        assertEquals(-1.0, intersections[0].time)
        assertEquals(1.0, intersections[1].time)
    }

    @Test
    fun testRayMissesCubeFromNegativeX() {
        val cube = Cube()
        val ray = Ray(point(-2.0, 0.0, 0.0), vector(0.2673, 0.5345, 0.8018))
        val intersections = cube.localIntersect(ray)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testRayMissesCubeFromNegativeY() {
        val cube = Cube()
        val ray = Ray(point(0.0, -2.0, 0.0), vector(0.8018, 0.2673, 0.5345))
        val intersections = cube.localIntersect(ray)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testRayMissesCubeFromNegativeZ() {
        val cube = Cube()
        val ray = Ray(point(0.0, 0.0, -2.0), vector(0.5345, 0.8018, 0.2673))
        val intersections = cube.localIntersect(ray)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testRayMissesCubeFromPositiveXAndZ() {
        val cube = Cube()
        val ray = Ray(point(2.0, 0.0, 2.0), vector(0.0, 0.0, -1.0))
        val intersections = cube.localIntersect(ray)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testRayMissesCubeFromPositiveYAndZ() {
        val cube = Cube()
        val ray = Ray(point(0.0, 2.0, 2.0), vector(0.0, -1.0, 0.0))
        val intersections = cube.localIntersect(ray)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testRayMissesCubeFromPositiveXAndY() {
        val cube = Cube()
        val ray = Ray(point(2.0, 2.0, 0.0), vector(-1.0, 0.0, 0.0))
        val intersections = cube.localIntersect(ray)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testNormalOnSurfaceOfCube() {
        val cube = Cube()
        assertEquals(vector(1.0, 0.0, 0.0), cube.localNormalAt(point(1.0, 0.5, -0.8)))
        assertEquals(vector(-1.0, 0.0, 0.0), cube.localNormalAt(point(-1.0, -0.2, 0.9)))
        assertEquals(vector(0.0, 1.0, 0.0), cube.localNormalAt(point(-0.4, 1.0, -0.1)))
        assertEquals(vector(0.0, -1.0, 0.0), cube.localNormalAt(point(0.3, -1.0, -0.7)))
        assertEquals(vector(0.0, 0.0, 1.0), cube.localNormalAt(point(-0.6, 0.3, 1.0)))
        assertEquals(vector(0.0, 0.0, -1.0), cube.localNormalAt(point(0.4, 0.4, -1.0)))
        assertEquals(vector(1.0, 0.0, 0.0), cube.localNormalAt(point(1.0, 1.0, 1.0)))
        assertEquals(vector(-1.0, 0.0, 0.0), cube.localNormalAt(point(-1.0, -1.0, -1.0)))
    }

    @Test
    fun testBoundingBox() {
        val cube = Cube()
        val boundingBox = cube.localBoundingBox()
        assertEquals(point(-1.0, -1.0, -1.0), boundingBox.min)
        assertEquals(point(1.0, 1.0, 1.0), boundingBox.max)
    }

}
