package raytracer.elements

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import raytracer.elements.Tuple.Companion.origin
import raytracer.elements.Tuple.Companion.point
import raytracer.elements.Tuple.Companion.vector

internal class GroupTest {

    @Test
    fun testAddShapeToGroup() {
        val shape = createShape()
        val group = Group(shapes = listOf(shape))
        assertEquals(group, shape.group)
    }

    @Test
    fun testIntersectRayWithEmptyGroup() {
        val group = Group()
        val ray = Ray(point(0.0, 0.0, 0.0), vector(0.0, 0.0, 1.0))
        val intersections = group.localIntersect(ray)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testIntersectRayWithNonemptyGroup() {
        val sphere1 = Sphere()
        val sphere2 = Sphere(transformationMatrix = Matrix.translation(0.0, 0.0, -3.0))
        val sphere3 = Sphere(transformationMatrix = Matrix.translation(5.0, 0.0, 0.0))
        val group = Group(shapes = listOf(sphere1, sphere2, sphere3))
        val ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        val intersections = group.localIntersect(ray)
        assertEquals(4, intersections.count)
        assertEquals(sphere2, intersections[0].shape)
        assertEquals(sphere2, intersections[1].shape)
        assertEquals(sphere1, intersections[2].shape)
        assertEquals(sphere1, intersections[3].shape)
    }

    @Test
    fun testIntersectTransformedGroup() {
        val sphere = Sphere(transformationMatrix = Matrix.translation(5.0, 0.0, 0.0))
        val group = Group(
            transformationMatrix = Matrix.scaling(2.0, 2.0, 2.0),
            shapes = listOf(sphere)
        )
        val ray = Ray(point(10.0, 0.0, -10.0), vector(0.0, 0.0, 1.0))
        val intersections = group.intersect(ray)
        assertEquals(2, intersections.count)
    }

    @Test
    fun testLocalNormalAtIsNotSupportedByGroup() {
        assertThrows(UnsupportedOperationException::class.java) { Group().localNormalAt(point(1.0, 0.0, 0.0)) }
    }

    @Test
    fun testBoundingBoxOfEmptyGroup() {
        val group = Group()
        val boundingBox = group.localBoundingBox()
        assertEquals(origin, boundingBox.min)
        assertEquals(origin, boundingBox.max)
    }

    @Test
    fun testBoundingBox() {
        val group = Group(
            shapes = listOf(
                Sphere(Matrix.rotationZ(0.5) * Matrix.scaling(2.0, 3.0, 4.0)),
                Cylinder(transformationMatrix = Matrix.scaling(1.0, 1.0, 8.0), minimum = -3.0, maximum = 4.0)
            )
        )
        val boundingBox = group.localBoundingBox()
        assertEquals(point(-3.19344, -3.59159, -8.0), boundingBox.min)
        assertEquals(point(3.19344, 4.0, 8.0), boundingBox.max)
    }

    @Test
    fun testIntersectionOnShapeIsNotCalledWhenRayMissesBoundingBox() {
        val shape = object : Shape(Matrix.identity(4), Material()) {
            override fun localIntersect(ray: Ray) = throw IllegalAccessException()
            override fun localNormalAt(point: Tuple) = point.asVector()
            override fun localBoundingBox() = BoundingBox(point(-1.0, -1.0, -1.0), point(1.0, 1.0, 1.0))
        }
        val group = Group(shapes = listOf(shape))
        val ray = Ray(point(2.0, 2.0, 0.0), vector(-1.0, 0.0, 0.0))
        assertDoesNotThrow { group.intersect(ray) }
    }

    private fun createShape() = object : Shape(Matrix.identity(4), Material()) {
        override fun localIntersect(ray: Ray) = Intersections()
        override fun localNormalAt(point: Tuple) = point.asVector()
        override fun localBoundingBox() = BoundingBox(origin, origin)
    }

}
