package raytracer.elements

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class WorldTest {

    @Test
    fun testShadingIntersection() {
        val world = World()
        val ray = Ray(Tuple.point(0.0, 0.0, -5.0), Tuple.vector(0.0, 0.0, 1.0))
        val sphere = world.shapes[0]
        val intersection = Intersection(4.0, sphere)
        val computation = ShadingPreComputation.of(ray, intersection)
        assertEquals(Tuple.color(0.38066, 0.47583, 0.2855), world.shadeHit(computation))
    }

    @Test
    fun testShadingIntersectionFromInside() {
        val world = World(pointLight = PointLight(Tuple.point(0.0, 0.25, 0.0), Tuple.color(1.0, 1.0, 1.0)))
        val ray = Ray(Tuple.point(0.0, 0.0, 0.0), Tuple.vector(0.0, 0.0, 1.0))
        val sphere = world.shapes[1]
        val intersection = Intersection(0.5, sphere)
        val computation = ShadingPreComputation.of(ray, intersection)
        assertEquals(Tuple.color(0.90498, 0.90498, 0.90498), world.shadeHit(computation))
    }

    @Test
    fun testShadeHitWithIntersectionInShadow() {
        val world = World(
            pointLight = PointLight(Tuple.point(0.0, 0.0, -10.0), Tuple.color(1.0, 1.0, 1.0)),
            shapes = listOf(Sphere(), Sphere(transformationMatrix = Matrix.translation(0.0, 0.0, 10.0)))
        )
        val ray = Ray(Tuple.point(0.0, 0.0, 5.0), Tuple.vector(0.0, 0.0, 1.0))
        val intersection = Intersection(4.0, world.shapes[1])
        val computation = ShadingPreComputation.of(ray, intersection)
        assertEquals(Tuple.color(0.1, 0.1, 0.1), world.shadeHit(computation))
    }

    @Test
    fun testColorWhenRayMisses() {
        val world = World()
        val ray = Ray(Tuple.point(0.0, 0.0, -5.0), Tuple.vector(0.0, 1.0, 0.0))
        assertEquals(Tuple.color(0.0, 0.0, 0.0), world.colorAtIntersection(ray))
    }

    @Test
    fun testColorWhenRayHits() {
        val world = World()
        val ray = Ray(Tuple.point(0.0, 0.0, -5.0), Tuple.vector(0.0, 0.0, 1.0))
        assertEquals(Tuple.color(0.38066, 0.47583, 0.2855), world.colorAtIntersection(ray))
    }

    @Test
    fun testColorWhithIntersectionBehindRay() {
        val material = Material(
            surfaceColor = Tuple.color(1.0, 1.0, 1.0),
            ambientReflection = 1.0,
            diffuseReflection = 0.9,
            specularReflection = 0.9,
            shininess = 200.0
        )
        val outerSphere = Sphere(Matrix.identity(4), material)
        val innerSphere = Sphere(Matrix.scaling(0.5, 0.5, 0.5), material)
        val world = World(shapes = listOf(outerSphere, innerSphere))
        val ray = Ray(Tuple.point(0.0, 0.0, 0.75), Tuple.vector(0.0, 0.0, -1.0))
        assertEquals(innerSphere.material.surfaceColor, world.colorAtIntersection(ray))
    }

    @Test
    fun testNoShadowWhenNothingIsCollinearWithPointAndLight() {
        val world = World()
        assertFalse(world.isShadowed(Tuple.point(0.0, 10.0, 0.0)))
    }

    @Test
    fun testShadowWhenObjectIsBetweenPointAndLight() {
        val world = World()
        assertTrue(world.isShadowed(Tuple.point(10.0, -10.0, 10.0)))
    }

    @Test
    fun testNoShadowWhenObjectIsBehindPoint() {
        val world = World()
        assertFalse(world.isShadowed(Tuple.point(-2.0, 2.0, -2.0)))
    }

    @Test
    fun testIsShadowedMustBeCalledWithPoint() {
        assertThrows(IllegalArgumentException::class.java) { World().isShadowed(Tuple.vector(1.0, 1.0, 1.0)) }
    }

    @Test
    fun testIntersectingDefaultWorld() {
        val world = World()
        val ray = Ray(Tuple.point(0.0, 0.0, -5.0), Tuple.vector(0.0, 0.0, 1.0))
        val intersections = world.intersect(ray)
        assertEquals(4, intersections.count)
        assertEquals(4.0, intersections[0].time)
        assertEquals(4.5, intersections[1].time)
        assertEquals(5.5, intersections[2].time)
        assertEquals(6.0, intersections[3].time)
    }

}
