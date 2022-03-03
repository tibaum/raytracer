package raytracer.elements

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class IntersectionTest {

    @Test
    fun testConstruction() {
        val sphere = Sphere()
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
        val sphere = Sphere()
        val i1 = Intersection(1.0, sphere)
        val i2 = Intersection(2.0, sphere)
        val intersections = Intersections(i1, i2)
        assertEquals(2, intersections.count)
        assertEquals(1.0, intersections[0].time)
        assertEquals(2.0, intersections[1].time)
    }

    @Test
    fun testIntersectionComparison() {
        val sphere = Sphere()
        val i1 = Intersection(1.0, sphere)
        val i2 = Intersection(2.0, sphere)
        val i3 = Intersection(2.0, sphere)
        assertEquals(-1, i1.compareTo(i2))
        assertEquals(0, i1.compareTo(i1))
        assertEquals(1, i2.compareTo(i1))
        assertEquals(0, i2.compareTo(i3))
    }

    @Test
    fun testIntersectionsAreSorted() {
        val sphere = Sphere()
        val i1 = Intersection(7.0, sphere)
        val i2 = Intersection(3.0, sphere)
        val i3 = Intersection(-2.0, sphere)
        val i4 = Intersection(5.0, sphere)
        val i5 = Intersection(3.0, sphere)
        val intersections = Intersections(i1, i2, i3, i4, i5)
        assertEquals(5, intersections.count)
        assertEquals(i3, intersections[0])
        assertEquals(i2, intersections[1])
        assertEquals(i5, intersections[2])
        assertEquals(i4, intersections[3])
        assertEquals(i1, intersections[4])
    }

    @Test
    fun testHitAllIntersectionsWithPositiveTime() {
        val sphere = Sphere()
        val i1 = Intersection(1.0, sphere)
        val i2 = Intersection(2.0, sphere)
        val intersections = Intersections(i2, i1)
        assertEquals(i1, intersections.hit())
    }

    @Test
    fun testHitSomeIntersectionsWithNegativeTime() {
        val sphere = Sphere()
        val i1 = Intersection(-1.0, sphere)
        val i2 = Intersection(1.0, sphere)
        val intersections = Intersections(i2, i1)
        assertEquals(i2, intersections.hit())
    }

    @Test
    fun testHitAllIntersectionsWithNegativeTime() {
        val sphere = Sphere()
        val i1 = Intersection(-2.0, sphere)
        val i2 = Intersection(-1.0, sphere)
        val intersections = Intersections(i2, i1)
        assertEquals(null, intersections.hit())
    }

    @Test
    fun testHitIsAlwaysTheLowestNonNegativeValue() {
        val sphere = Sphere()
        val i1 = Intersection(5.0, sphere)
        val i2 = Intersection(7.0, sphere)
        val i3 = Intersection(-3.0, sphere)
        val i4 = Intersection(2.0, sphere)
        val intersections = Intersections(i1, i2, i3, i4)
        assertEquals(i4, intersections.hit())
    }

    @Test
    fun testHitWithZeroAsTheLowestNonNegativeValue() {
        val sphere = Sphere()
        val i1 = Intersection(-3.0, sphere)
        val i2 = Intersection(0.0, sphere)
        val intersections = Intersections(i1, i2)
        assertEquals(i2, intersections.hit())
    }

    @Test
    fun testHitWithNoIntersectionAvailable() {
        val intersections = Intersections()
        assertEquals(null, intersections.hit())
    }

    @Test
    fun testAccumulationOfEmptyIntersections() {
        val intersections = Intersections()
        val accumulation = intersections.accumulate(Intersections())
        assertEquals(0, accumulation.count)
    }

    @Test
    fun testAccumulationOfIntersectionsIsSorted() {
        val sphere = Sphere()
        val i1 = Intersection(7.0, sphere)
        val i2 = Intersection(3.0, sphere)
        val i3 = Intersection(-2.0, sphere)
        val intersections1 = Intersections(i1)
        val intersections2 = Intersections(i2, i3)
        val accumulation = intersections1.accumulate(intersections2)
        assertEquals(3, accumulation.count)
        assertEquals(i3, accumulation[0])
        assertEquals(i2, accumulation[1])
        assertEquals(i1, accumulation[2])
    }

}
