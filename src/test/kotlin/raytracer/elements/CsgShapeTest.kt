package raytracer.elements

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import raytracer.elements.CsgOperation.*
import raytracer.elements.CsgShape.Companion.intersectionAllowed
import raytracer.elements.Tuple.Companion.point
import raytracer.elements.Tuple.Companion.vector
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class CsgShapeTest {

    @Test
    fun testConstruction() {
        val sphere = Sphere()
        val cube = Cube()
        val csgShape = CsgShape(left = sphere, right = cube, operation = UNION)
        assertEquals(UNION, csgShape.operation)
        assertEquals(sphere, csgShape.left)
        assertEquals(cube, csgShape.right)
        assertEquals(sphere.parent, csgShape)
        assertEquals(cube.parent, csgShape)
    }

    @Test
    fun testUnionAllowed() {
        assertFalse { intersectionAllowed(UNION, leftHit = true, hitInsideLeft = true, hitInsideRight = true) }
        assertTrue { intersectionAllowed(UNION, leftHit = true, hitInsideLeft = true, hitInsideRight = false) }
        assertFalse { intersectionAllowed(UNION, leftHit = true, hitInsideLeft = false, hitInsideRight = true) }
        assertTrue { intersectionAllowed(UNION, leftHit = true, hitInsideLeft = false, hitInsideRight = false) }
        assertFalse { intersectionAllowed(UNION, leftHit = false, hitInsideLeft = true, hitInsideRight = true) }
        assertFalse { intersectionAllowed(UNION, leftHit = false, hitInsideLeft = true, hitInsideRight = false) }
        assertTrue { intersectionAllowed(UNION, leftHit = false, hitInsideLeft = false, hitInsideRight = true) }
        assertTrue { intersectionAllowed(UNION, leftHit = false, hitInsideLeft = false, hitInsideRight = false) }
    }

    @Test
    fun testIntersectionAllowed() {
        assertTrue { intersectionAllowed(INTERSECTION, leftHit = true, hitInsideLeft = true, hitInsideRight = true) }
        assertFalse { intersectionAllowed(INTERSECTION, leftHit = true, hitInsideLeft = true, hitInsideRight = false) }
        assertTrue { intersectionAllowed(INTERSECTION, leftHit = true, hitInsideLeft = false, hitInsideRight = true) }
        assertFalse { intersectionAllowed(INTERSECTION, leftHit = true, hitInsideLeft = false, hitInsideRight = false) }
        assertTrue { intersectionAllowed(INTERSECTION, leftHit = false, hitInsideLeft = true, hitInsideRight = true) }
        assertTrue { intersectionAllowed(INTERSECTION, leftHit = false, hitInsideLeft = true, hitInsideRight = false) }
        assertFalse { intersectionAllowed(INTERSECTION, leftHit = false, hitInsideLeft = false, hitInsideRight = true) }
        assertFalse {
            intersectionAllowed(INTERSECTION, leftHit = false, hitInsideLeft = false, hitInsideRight = false)
        }
    }

    @Test
    fun testDifferenceAllowed() {
        assertFalse { intersectionAllowed(DIFFERENCE, leftHit = true, hitInsideLeft = true, hitInsideRight = true) }
        assertTrue { intersectionAllowed(DIFFERENCE, leftHit = true, hitInsideLeft = true, hitInsideRight = false) }
        assertFalse { intersectionAllowed(DIFFERENCE, leftHit = true, hitInsideLeft = false, hitInsideRight = true) }
        assertTrue { intersectionAllowed(DIFFERENCE, leftHit = true, hitInsideLeft = false, hitInsideRight = false) }
        assertTrue { intersectionAllowed(DIFFERENCE, leftHit = false, hitInsideLeft = true, hitInsideRight = true) }
        assertTrue { intersectionAllowed(DIFFERENCE, leftHit = false, hitInsideLeft = true, hitInsideRight = false) }
        assertFalse { intersectionAllowed(DIFFERENCE, leftHit = false, hitInsideLeft = false, hitInsideRight = true) }
        assertFalse { intersectionAllowed(DIFFERENCE, leftHit = false, hitInsideLeft = false, hitInsideRight = false) }
    }

    @Test
    fun testFilteringIntersectionsWithUnion() {
        val sphere = Sphere()
        val cube = Cube()
        val csgShape = CsgShape(left = sphere, right = cube, operation = UNION)
        val intersections = Intersections(
            Intersection(1.0, sphere),
            Intersection(2.0, cube),
            Intersection(3.0, sphere),
            Intersection(4.0, cube)
        )
        val result = csgShape.filterIntersections(intersections)
        assertEquals(2, result.count)
        assertEquals(intersections[0], result[0])
        assertEquals(intersections[3], result[1])
    }

    @Test
    fun testFilteringIntersectionsWithIntersection() {
        val sphere = Sphere()
        val cube = Cube()
        val csgShape = CsgShape(left = sphere, right = cube, operation = INTERSECTION)
        val intersections = Intersections(
            Intersection(1.0, sphere),
            Intersection(2.0, cube),
            Intersection(3.0, sphere),
            Intersection(4.0, cube)
        )
        val result = csgShape.filterIntersections(intersections)
        assertEquals(2, result.count)
        assertEquals(intersections[1], result[0])
        assertEquals(intersections[2], result[1])
    }

    @Test
    fun testFilteringIntersectionsWithDifference() {
        val sphere = Sphere()
        val cube = Cube()
        val csgShape = CsgShape(left = sphere, right = cube, operation = DIFFERENCE)
        val intersections = Intersections(
            Intersection(1.0, sphere),
            Intersection(2.0, cube),
            Intersection(3.0, sphere),
            Intersection(4.0, cube)
        )
        val result = csgShape.filterIntersections(intersections)
        assertEquals(2, result.count)
        assertEquals(intersections[0], result[0])
        assertEquals(intersections[1], result[1])
    }

    @Test
    fun testIncludesShape() {
        val sphere = Sphere()
        val cube = Cube()
        val cone = Cone()
        val cylinder = Cylinder()
        val group = Group(shapes = listOf(sphere))
        val csgShape1 = CsgShape(left = group, right = cube, operation = DIFFERENCE)
        val csgShape2 = CsgShape(left = cone, right = csgShape1, operation = DIFFERENCE)
        val csgShape3 = CsgShape(left = csgShape2, right = cylinder, operation = DIFFERENCE)

        assertTrue { csgShape1.includes(sphere) }
        assertTrue { csgShape1.includes(cube) }
        assertFalse { csgShape1.includes(cone) }
        assertFalse { csgShape1.includes(cylinder) }
        assertTrue { csgShape1.includes(group) }
        assertTrue { csgShape1.includes(csgShape1) }
        assertFalse { csgShape1.includes(csgShape2) }
        assertFalse { csgShape1.includes(csgShape3) }

        assertTrue { csgShape2.includes(sphere) }
        assertTrue { csgShape2.includes(cube) }
        assertTrue { csgShape2.includes(cone) }
        assertFalse { csgShape2.includes(cylinder) }
        assertTrue { csgShape2.includes(group) }
        assertTrue { csgShape2.includes(csgShape1) }
        assertTrue { csgShape2.includes(csgShape2) }
        assertFalse { csgShape2.includes(csgShape3) }

        assertTrue { csgShape3.includes(cube) }
        assertTrue { csgShape3.includes(cone) }
        assertTrue { csgShape3.includes(cylinder) }
        assertTrue { csgShape3.includes(sphere) }
        assertFalse { csgShape3.includes(Sphere()) }
        assertFalse { csgShape3.includes(Group(shapes = listOf())) }
        assertFalse { csgShape3.includes(CsgShape(left = cone, right = cube, operation = DIFFERENCE)) }
        assertTrue { csgShape3.includes(group) }
        assertTrue { csgShape3.includes(csgShape1) }
        assertTrue { csgShape3.includes(csgShape2) }
        assertTrue { csgShape3.includes(csgShape3) }
    }

    @Test
    fun testRayMissesCsgShape() {
        val csgShape = CsgShape(left = Sphere(), right = Cube(), operation = UNION)
        val ray = Ray(point(0.0, 2.0, -5.0), vector(0.0, 0.0, 1.0))
        val intersections = csgShape.localIntersect(ray)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testRayHitsCsgShape() {
        val sphere1 = Sphere()
        val sphere2 = Sphere(Matrix.translation(0.0, 0.0, 0.5))
        val csgShape = CsgShape(left = sphere1, right = sphere2, UNION)
        val ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        val intersections = csgShape.localIntersect(ray)
        assertEquals(2, intersections.count)
        assertEquals(4.0, intersections[0].time)
        assertEquals(sphere1, intersections[0].shape)
        assertEquals(6.5, intersections[1].time)
        assertEquals(sphere2, intersections[1].shape)
    }

    @Test
    fun testBoundingBox() {
        val cylinder1 = Cylinder(minimum = -2.0, maximum = 5.0)
        val cylinder2 = Cylinder(minimum = -3.0, maximum = 4.0)
        val csgShape = CsgShape(left = cylinder1, right = cylinder2, operation = UNION)
        val boundingBox = csgShape.localBoundingBox()
        assertEquals(point(-1.0, -3.0, -1.0), boundingBox.min)
        assertEquals(point(1.0, 5.0, 1.0), boundingBox.max)
    }

    @Test
    fun testLocalNormalAtIsNotSupportedByCsgShape() {
        val csgShape = CsgShape(left = Sphere(), right = Cube(), operation = UNION)
        assertThrows(UnsupportedOperationException::class.java) { csgShape.localNormalAt(point(1.0, 0.0, 0.0)) }
    }

}
