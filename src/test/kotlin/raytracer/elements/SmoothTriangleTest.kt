package raytracer.elements

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import raytracer.elements.Tuple.Companion.point
import raytracer.elements.Tuple.Companion.vector

internal class SmoothTriangleTest {

    @Test
    fun testConstruction() {
        assertThrows(IllegalArgumentException::class.java) {
            SmoothTriangle(
                point1 = vector(0.0, 1.0, 0.0),
                point2 = point(-1.0, 0.0, 0.0),
                point3 = point(1.0, 0.0, 0.0),
                normal1 = vector(1.0, 0.0, 0.0),
                normal2 = vector(1.0, 0.0, 0.0),
                normal3 = vector(1.0, 0.0, 0.0)
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            SmoothTriangle(
                point1 = point(0.0, 1.0, 0.0),
                point2 = vector(-1.0, 0.0, 0.0),
                point3 = point(1.0, 0.0, 0.0),
                normal1 = vector(1.0, 0.0, 0.0),
                normal2 = vector(1.0, 0.0, 0.0),
                normal3 = vector(1.0, 0.0, 0.0)
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            SmoothTriangle(
                point1 = point(0.0, 1.0, 0.0),
                point2 = point(-1.0, 0.0, 0.0),
                point3 = vector(1.0, 0.0, 0.0),
                normal1 = vector(1.0, 0.0, 0.0),
                normal2 = vector(1.0, 0.0, 0.0),
                normal3 = vector(1.0, 0.0, 0.0)
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            SmoothTriangle(
                point1 = point(0.0, 1.0, 0.0),
                point2 = point(-1.0, 0.0, 0.0),
                point3 = point(1.0, 0.0, 0.0),
                normal1 = point(1.0, 0.0, 0.0),
                normal2 = vector(1.0, 0.0, 0.0),
                normal3 = vector(1.0, 0.0, 0.0)
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            SmoothTriangle(
                point1 = point(0.0, 1.0, 0.0),
                point2 = point(-1.0, 0.0, 0.0),
                point3 = point(1.0, 0.0, 0.0),
                normal1 = vector(1.0, 0.0, 0.0),
                normal2 = point(1.0, 0.0, 0.0),
                normal3 = vector(1.0, 0.0, 0.0)
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            SmoothTriangle(
                point1 = point(0.0, 1.0, 0.0),
                point2 = point(-1.0, 0.0, 0.0),
                point3 = point(1.0, 0.0, 0.0),
                normal1 = vector(1.0, 0.0, 0.0),
                normal2 = vector(1.0, 0.0, 0.0),
                normal3 = point(1.0, 0.0, 0.0)
            )
        }
    }

    @Test
    fun testIntersectionWithUandV() {
        val triangle = SmoothTriangle(
            point1 = point(0.0, 1.0, 0.0),
            point2 = point(-1.0, 0.0, 0.0),
            point3 = point(1.0, 0.0, 0.0),
            normal1 = vector(0.0, 1.0, 0.0),
            normal2 = vector(-1.0, 0.0, 0.0),
            normal3 = vector(1.0, 0.0, 0.0)
        )
        val ray = Ray(point(-0.2, 0.3, -2.0), vector(0.0, 0.0, 1.0))
        val intersections = triangle.localIntersect(ray)
        assertEquals(1, intersections.count)
        assertEquals(2.0, intersections[0].time)
        assertTrue(almostEqual(0.45, intersections[0].u!!))
        assertTrue(almostEqual(0.25, intersections[0].v!!))
    }

    @Test
    fun testNormalAt() {
        val triangle = SmoothTriangle(
            point1 = point(0.0, 1.0, 0.0),
            point2 = point(-1.0, 0.0, 0.0),
            point3 = point(1.0, 0.0, 0.0),
            normal1 = vector(0.0, 1.0, 0.0),
            normal2 = vector(-1.0, 0.0, 0.0),
            normal3 = vector(1.0, 0.0, 0.0)
        )
        val hit = Intersection(1.0, triangle, 0.45, 0.25)
        val normal = triangle.normalAt(point(0.0, 0.0, 0.0), hit)
        assertEquals(vector(-0.5547, 0.83205, 0.0), normal)
    }

    @Test
    fun testLocalNormalAtWithoutProvidingIntersection() {
        val triangle = SmoothTriangle(
            point1 = point(0.0, 1.0, 0.0),
            point2 = point(-1.0, 0.0, 0.0),
            point3 = point(1.0, 0.0, 0.0),
            normal1 = vector(0.0, 1.0, 0.0),
            normal2 = vector(-1.0, 0.0, 0.0),
            normal3 = vector(1.0, 0.0, 0.0)
        )
        assertThrows(IllegalArgumentException::class.java) { triangle.localNormalAt(point(0.0, 0.0, 0.0)) }
    }

    @Test
    fun testLocalNormalAtProvidingIntersectionWithoutU() {
        val triangle = SmoothTriangle(
            point1 = point(0.0, 1.0, 0.0),
            point2 = point(-1.0, 0.0, 0.0),
            point3 = point(1.0, 0.0, 0.0),
            normal1 = vector(0.0, 1.0, 0.0),
            normal2 = vector(-1.0, 0.0, 0.0),
            normal3 = vector(1.0, 0.0, 0.0)
        )
        val hit = Intersection(1.0, triangle, u = null, v = 0.25)
        assertThrows(IllegalArgumentException::class.java) { triangle.localNormalAt(point(0.0, 0.0, 0.0), hit) }
    }

    @Test
    fun testLocalNormalAtProvidingIntersectionWithoutV() {
        val triangle = SmoothTriangle(
            point1 = point(0.0, 1.0, 0.0),
            point2 = point(-1.0, 0.0, 0.0),
            point3 = point(1.0, 0.0, 0.0),
            normal1 = vector(0.0, 1.0, 0.0),
            normal2 = vector(-1.0, 0.0, 0.0),
            normal3 = vector(1.0, 0.0, 0.0)
        )
        val hit = Intersection(1.0, triangle, u = 0.45, v = null)
        assertThrows(IllegalArgumentException::class.java) { triangle.localNormalAt(point(0.0, 0.0, 0.0), hit) }
    }

}
