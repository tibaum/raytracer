package raytracer.elements

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class IntersectionTest {

    @Test
    fun testConstruction() {
        val sphere = Sphere.unit()
        val time = 3.5
        val intersection = Intersection(time, sphere)
        assertEquals(time, intersection.time)
        assertEquals(sphere, intersection.sphere)
    }

    @Test
    fun testEmptyIntersections() {
        assertEquals(0, Intersections().count)
    }

    @Test
    fun testAggregatingIntersections() {
        val sphere = Sphere.unit()
        val i1 = Intersection(1.0, sphere)
        val i2 = Intersection(2.0, sphere)
        val intersections = Intersections(i1, i2)
        assertEquals(2, intersections.count)
        assertEquals(1.0, intersections[0].time)
        assertEquals(2.0, intersections[1].time)
    }

}
