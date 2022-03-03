package raytracer.elements

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class WorldTest {

    @Test
    fun testShadingIntersection() {
        val world = World()
        val ray = Ray(Tuple.point(0.0, 0.0, -5.0), Tuple.vector(0.0, 0.0, 1.0))
        val sphere = world.spheres[0]
        val intersection = Intersection(4.0, sphere)
        val computation = ShadingPreComputation.of(ray, intersection)
        assertEquals(Tuple.color(0.38066, 0.47583, 0.2855), world.shadeHit(computation))
    }

    @Test
    fun testShadingIntersectionFromInside() {
        val world = World(pointLight = PointLight(Tuple.point(0.0, 0.25, 0.0), Tuple.color(1.0, 1.0, 1.0)))
        val ray = Ray(Tuple.point(0.0, 0.0, 0.0), Tuple.vector(0.0, 0.0, 1.0))
        val sphere = world.spheres[1]
        val intersection = Intersection(0.5, sphere)
        val computation = ShadingPreComputation.of(ray, intersection)
        assertEquals(Tuple.color(0.90498, 0.90498, 0.90498), world.shadeHit(computation))
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
        val world = World(spheres = listOf(outerSphere, innerSphere))
        val ray = Ray(Tuple.point(0.0, 0.0, 0.75), Tuple.vector(0.0, 0.0, -1.0))
        assertEquals(innerSphere.material.surfaceColor, world.colorAtIntersection(ray))
    }

}
