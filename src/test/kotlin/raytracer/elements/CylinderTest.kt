package raytracer.elements

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import raytracer.elements.Tuple.Companion.point
import raytracer.elements.Tuple.Companion.vector
import kotlin.Double.Companion.NEGATIVE_INFINITY
import kotlin.Double.Companion.POSITIVE_INFINITY
import kotlin.math.abs

internal class CylinderTest {

    @Test
    fun testInfiniteCylinderCannotBeClosed() {
        assertThrows(IllegalArgumentException::class.java) { Cylinder(closed = true) }
        assertThrows(IllegalArgumentException::class.java) { Cylinder(minimum = 1.0, closed = true) }
        assertThrows(IllegalArgumentException::class.java) { Cylinder(maximum = 1.0, closed = true) }
    }

    @Test
    fun testRayPositionedOnSurfaceMissesCylinder() {
        val cylinder = Cylinder()
        val ray = Ray(point(1.0, 0.0, 0.0), vector(0.0, 1.0, 0.0).normalize())
        val intersections = cylinder.localIntersect(ray)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testRayPositionedInsideMissesCylinder() {
        val cylinder = Cylinder()
        val ray = Ray(point(0.0, 0.0, 0.0), vector(0.0, 1.0, 0.0).normalize())
        val intersections = cylinder.localIntersect(ray)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testRayPositionedOutsideMissesCylinder() {
        val cylinder = Cylinder()
        val ray = Ray(point(0.0, 0.0, -5.0), vector(1.0, 1.0, 1.0).normalize())
        val intersections = cylinder.localIntersect(ray)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testRayHitsCylinderOnTangent() {
        val cylinder = Cylinder()
        val ray = Ray(point(1.0, 0.0, -5.0), vector(0.0, 0.0, 1.0).normalize())
        val intersections = cylinder.localIntersect(ray)
        assertEquals(2, intersections.count)
        assertEquals(5.0, intersections[0].time)
        assertEquals(5.0, intersections[1].time)
    }

    @Test
    fun testRayHitsCylinderPependicular() {
        val cylinder = Cylinder()
        val ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0).normalize())
        val intersections = cylinder.localIntersect(ray)
        assertEquals(2, intersections.count)
        assertEquals(4.0, intersections[0].time)
        assertEquals(6.0, intersections[1].time)
    }

    @Test
    fun testRayHitsCylinderAtAngle() {
        val cylinder = Cylinder()
        val ray = Ray(point(0.5, 0.0, -5.0), vector(0.1, 1.0, 1.0).normalize())
        val intersections = cylinder.localIntersect(ray)
        assertEquals(2, intersections.count)
        assertTrue { abs(6.80798 - intersections[0].time) < EPSILON }
        assertTrue { abs(7.08872 - intersections[1].time) < EPSILON }
    }

    @Test
    fun testNormalOnPositiveX() {
        val cylinder = Cylinder()
        assertEquals(vector(1.0, 0.0, 0.0), cylinder.localNormalAt(point(1.0, 0.0, 0.0)))
    }

    @Test
    fun testNormalOnNegativeZ() {
        val cylinder = Cylinder()
        assertEquals(vector(0.0, 0.0, -1.0), cylinder.localNormalAt(point(0.0, 5.0, -1.0)))
    }

    @Test
    fun testNormalOnPositiveZ() {
        val cylinder = Cylinder()
        assertEquals(vector(0.0, 0.0, 1.0), cylinder.localNormalAt(point(0.0, -2.0, 1.0)))
    }

    @Test
    fun testNormalOnNegativeX() {
        val cylinder = Cylinder()
        assertEquals(vector(-1.0, 0.0, 0.0), cylinder.localNormalAt(point(-1.0, 1.0, 0.0)))
    }

    @Test
    fun testIntersectConstrainedCylinderRayInside() {
        val cylinder = Cylinder(minimum = 1.0, maximum = 2.0)
        val ray = Ray(point(0.0, 1.5, 0.0), vector(0.1, 1.0, 0.0).normalize())
        val intersections = cylinder.localIntersect(ray)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testIntersectConstrainedCylinderRayAbove() {
        val cylinder = Cylinder(minimum = 1.0, maximum = 2.0)
        val ray = Ray(point(0.0, 3.0, -5.0), vector(0.0, 0.0, 1.0).normalize())
        val intersections = cylinder.localIntersect(ray)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testIntersectConstrainedCylinderRayBelow() {
        val cylinder = Cylinder(minimum = 1.0, maximum = 2.0)
        val ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0).normalize())
        val intersections = cylinder.localIntersect(ray)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testIntersectConstrainedCylinderMaximumIsOutOfBounds() {
        val cylinder = Cylinder(minimum = 1.0, maximum = 2.0)
        val ray = Ray(point(0.0, 2.0, -5.0), vector(0.0, 0.0, 1.0).normalize())
        val intersections = cylinder.localIntersect(ray)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testIntersectConstrainedCylinderMinimumIsOutOfBounds() {
        val cylinder = Cylinder(minimum = 1.0, maximum = 2.0)
        val ray = Ray(point(0.0, 1.0, -5.0), vector(0.0, 0.0, 1.0).normalize())
        val intersections = cylinder.localIntersect(ray)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testIntersectConstrainedCylinderPerpendicular() {
        val cylinder = Cylinder(minimum = 1.0, maximum = 2.0)
        val ray = Ray(point(0.0, 1.5, -2.0), vector(0.0, 0.0, 1.0).normalize())
        val intersections = cylinder.localIntersect(ray)
        assertEquals(2, intersections.count)
    }

    @Test
    fun testIntersectCapsOfClosedCylinderFromAbovePerpendicular() {
        val cylinder = Cylinder(minimum = 1.0, maximum = 2.0, closed = true)
        val ray = Ray(point(0.0, 3.0, 0.0), vector(0.0, -1.0, 0.0).normalize())
        val intersections = cylinder.localIntersect(ray)
        assertEquals(2, intersections.count)
    }

    @Test
    fun testIntersectCapsOfClosedCylinderFromAboveDiagonally() {
        val cylinder = Cylinder(minimum = 1.0, maximum = 2.0, closed = true)
        val ray = Ray(point(0.0, 3.0, -2.0), vector(0.0, -1.0, 2.0).normalize())
        val intersections = cylinder.localIntersect(ray)
        assertEquals(2, intersections.count)
    }

    @Test
    fun testIntersectCapsOfClosedCylinderFromAboveCornerCase() {
        val cylinder = Cylinder(minimum = 1.0, maximum = 2.0, closed = true)
        val ray = Ray(point(0.0, 4.0, -2.0), vector(0.0, -1.0, 1.0).normalize())
        val intersections = cylinder.localIntersect(ray)
        assertEquals(2, intersections.count)
    }

    @Test
    fun testIntersectCapsOfClosedCylinderFromBelowDiagonally() {
        val cylinder = Cylinder(minimum = 1.0, maximum = 2.0, closed = true)
        val ray = Ray(point(0.0, 0.0, -2.0), vector(0.0, 1.0, 2.0).normalize())
        val intersections = cylinder.localIntersect(ray)
        assertEquals(2, intersections.count)
    }

    @Test
    fun testIntersectCapsOfClosedCylinderFromBelowCornerCase() {
        val cylinder = Cylinder(minimum = 1.0, maximum = 2.0, closed = true)
        val ray = Ray(point(0.0, -1.0, -2.0), vector(0.0, 1.0, 1.0).normalize())
        val intersections = cylinder.localIntersect(ray)
        assertEquals(2, intersections.count)
    }

    @Test
    fun testNormalOnEndCapsLowerSide() {
        val cylinder = Cylinder(minimum = 1.0, maximum = 2.0, closed = true)
        assertEquals(vector(0.0, -1.0, 0.0), cylinder.localNormalAt(point(0.0, 1.0, 0.0)))
        assertEquals(vector(0.0, -1.0, 0.0), cylinder.localNormalAt(point(0.5, 1.0, 0.0)))
        assertEquals(vector(0.0, -1.0, 0.0), cylinder.localNormalAt(point(0.0, 1.0, 0.5)))
    }

    @Test
    fun testNormalOnEndCapsUpperSide() {
        val cylinder = Cylinder(minimum = 1.0, maximum = 2.0, closed = true)
        assertEquals(vector(0.0, 1.0, 0.0), cylinder.localNormalAt(point(0.0, 2.0, 0.0)))
        assertEquals(vector(0.0, 1.0, 0.0), cylinder.localNormalAt(point(0.5, 2.0, 0.0)))
        assertEquals(vector(0.0, 1.0, 0.0), cylinder.localNormalAt(point(0.0, 2.0, 0.5)))
    }

    @Test
    fun testBoundingBoxForInfiniteCylinder() {
        val cylinder = Cylinder()
        val boundingBox = cylinder.localBoundingBox()
        assertEquals(-1.0, boundingBox.min[0])
        assertEquals(NEGATIVE_INFINITY, boundingBox.min[1])
        assertEquals(-1.0, boundingBox.min[2])
        assertEquals(1.0, boundingBox.min[3])
        assertEquals(1.0, boundingBox.max[0])
        assertEquals(POSITIVE_INFINITY, boundingBox.max[1])
        assertEquals(1.0, boundingBox.max[2])
        assertEquals(1.0, boundingBox.max[3])
    }

    @Test
    fun testBoundingBoxForCylinderWithInfiniteMinimum() {
        val cylinder = Cylinder(maximum = 2.0)
        val boundingBox = cylinder.localBoundingBox()
        assertEquals(-1.0, boundingBox.min[0])
        assertEquals(NEGATIVE_INFINITY, boundingBox.min[1])
        assertEquals(-1.0, boundingBox.min[2])
        assertEquals(1.0, boundingBox.min[3])
        assertEquals(1.0, boundingBox.max[0])
        assertEquals(2.0, boundingBox.max[1])
        assertEquals(1.0, boundingBox.max[2])
        assertEquals(1.0, boundingBox.max[3])
    }

    @Test
    fun testBoundingBoxForCylinderWithInfiniteMaximum() {
        val cylinder = Cylinder(minimum = -3.0)
        val boundingBox = cylinder.localBoundingBox()
        assertEquals(-1.0, boundingBox.min[0])
        assertEquals(-3.0, boundingBox.min[1])
        assertEquals(-1.0, boundingBox.min[2])
        assertEquals(1.0, boundingBox.min[3])
        assertEquals(1.0, boundingBox.max[0])
        assertEquals(POSITIVE_INFINITY, boundingBox.max[1])
        assertEquals(1.0, boundingBox.max[2])
        assertEquals(1.0, boundingBox.max[3])
    }

    @Test
    fun testBoundingBoxForBoundedCylinder() {
        val cylinder = Cylinder(minimum = -3.0, maximum = 4.0)
        val boundingBox = cylinder.localBoundingBox()
        assertEquals(point(-1.0, -3.0, -1.0), boundingBox.min)
        assertEquals(point(1.0, 4.0, 1.0), boundingBox.max)
    }

}
