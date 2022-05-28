package raytracer.elements

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import raytracer.elements.Tuple.Companion.point
import raytracer.elements.Tuple.Companion.vector

internal class TriangleTest {

    @Test
    fun testConstruction() {
        assertThrows(IllegalArgumentException::class.java) {
            Triangle(
                point1 = point(0.0, 1.0, 0.0).asVector(),
                point2 = point(-1.0, 0.0, 0.0),
                point3 = point(1.0, 0.0, 0.0)
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            Triangle(
                point1 = point(0.0, 1.0, 0.0),
                point2 = point(-1.0, 0.0, 0.0).asVector(),
                point3 = point(1.0, 0.0, 0.0)
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            Triangle(
                point1 = point(0.0, 1.0, 0.0),
                point2 = point(-1.0, 0.0, 0.0),
                point3 = point(1.0, 0.0, 0.0).asVector()
            )
        }
    }

    @Test
    fun testLocalNormalAt() {
        val triangle = Triangle(
            point1 = point(0.0, 1.0, 0.0),
            point2 = point(-1.0, 0.0, 0.0),
            point3 = point(1.0, 0.0, 0.0)
        )
        val normal = vector(0.0, 0.0, -1.0)
        assertEquals(normal, triangle.localNormalAt(point(0.0, 0.5, 0.0)))
        assertEquals(normal, triangle.localNormalAt(point(-0.5, 0.75, 0.0)))
        assertEquals(normal, triangle.localNormalAt(point(0.5, 0.25, 0.0)))
    }

    @Test
    fun testIntersectRayParallelToTriangle() {
        val triangle = Triangle(
            point1 = point(0.0, 1.0, 0.0),
            point2 = point(-1.0, 0.0, 0.0),
            point3 = point(1.0, 0.0, 0.0)
        )
        val ray = Ray(point(0.0, -1.0, 2.0), vector(0.0, 1.0, 0.0))
        val intersections = triangle.localIntersect(ray)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testIntersectRayMissesPoint1Point3Edge() {
        val triangle = Triangle(
            point1 = point(0.0, 1.0, 0.0),
            point2 = point(-1.0, 0.0, 0.0),
            point3 = point(1.0, 0.0, 0.0)
        )
        val ray = Ray(point(1.0, 1.0, -2.0), vector(0.0, 0.0, 1.0))
        val intersections = triangle.localIntersect(ray)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testIntersectRayMissesPoint1Point2Edge() {
        val triangle = Triangle(
            point1 = point(0.0, 1.0, 0.0),
            point2 = point(-1.0, 0.0, 0.0),
            point3 = point(1.0, 0.0, 0.0)
        )
        val ray = Ray(point(-1.0, 1.0, -2.0), vector(0.0, 0.0, 1.0))
        val intersections = triangle.localIntersect(ray)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testIntersectRayMissesPoint2Point3Edge() {
        val triangle = Triangle(
            point1 = point(0.0, 1.0, 0.0),
            point2 = point(-1.0, 0.0, 0.0),
            point3 = point(1.0, 0.0, 0.0)
        )
        val ray = Ray(point(0.0, -1.0, -2.0), vector(0.0, 0.0, 1.0))
        val intersections = triangle.localIntersect(ray)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testRayStrikesTriangle() {
        val triangle = Triangle(
            point1 = point(0.0, 1.0, 0.0),
            point2 = point(-1.0, 0.0, 0.0),
            point3 = point(1.0, 0.0, 0.0)
        )
        val ray = Ray(point(0.0, 0.5, -2.0), vector(0.0, 0.0, 1.0))
        val intersections = triangle.localIntersect(ray)
        assertEquals(1, intersections.count)
        assertEquals(2.0, intersections[0].time)
        assertEquals(0.25, intersections[0].u)
        assertEquals(0.25, intersections[0].v)
    }

    @Test
    fun testBoundingBox() {
        val triangle = Triangle(
            point1 = point(3.0, -7.0, 1.0),
            point2 = point(-1.0, 5.0, 2.0),
            point3 = point(-2.0, -1.0, 8.0)
        )
        val boundingBox = triangle.boundingBox()
        assertEquals(point(-2.0, -7.0, 1.0), boundingBox.min)
        assertEquals(point(3.0, 5.0, 8.0), boundingBox.max)
    }

}
