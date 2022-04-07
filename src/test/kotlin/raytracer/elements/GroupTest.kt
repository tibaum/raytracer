package raytracer.elements

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import raytracer.elements.Tuple.Companion.point
import raytracer.elements.Tuple.Companion.vector

internal class GroupTest {

    @Test
    fun testAddShapeToGroup() {
        val group = Group()
        val shape = createShape()
        group.add(shape)
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
        val group = Group()
        val sphere1 = Sphere()
        val sphere2 = Sphere(transformationMatrix = Matrix.translation(0.0, 0.0, -3.0))
        val sphere3 = Sphere(transformationMatrix = Matrix.translation(5.0, 0.0, 0.0))
        group.add(sphere1)
        group.add(sphere2)
        group.add(sphere3)
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
        val group = Group(transformationMatrix = Matrix.scaling(2.0, 2.0, 2.0))
        val sphere = Sphere(transformationMatrix = Matrix.translation(5.0, 0.0, 0.0))
        group.add(sphere)
        val ray = Ray(point(10.0, 0.0, -10.0), vector(0.0, 0.0, 1.0))
        val intersections = group.intersect(ray)
        assertEquals(2, intersections.count)
    }

    @Test
    fun testLocalNormalAtIsNotSupportedByGroup() {
        assertThrows(UnsupportedOperationException::class.java) { Group().localNormalAt(point(1.0, 0.0, 0.0)) }
    }

    private fun createShape() = object : Shape(Matrix.identity(4), Material()) {
        override fun localIntersect(ray: Ray) = Intersections()
        override fun localNormalAt(point: Tuple) = point.asVector()
    }

}
