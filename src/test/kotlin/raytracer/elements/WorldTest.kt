package raytracer.elements

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import raytracer.elements.Tuple.Companion.color
import raytracer.elements.Tuple.Companion.point
import raytracer.elements.Tuple.Companion.vector
import kotlin.math.sqrt

class WorldTest {

    @Test
    fun testShadingIntersection() {
        val world = World()
        val ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        val sphere = world.shapes[0]
        val intersection = Intersection(4.0, sphere)
        val computation = ShadingPreComputation.of(ray, intersection)
        assertEquals(color(0.38066, 0.47583, 0.2855), world.shadeHit(computation))
    }

    @Test
    fun testShadingIntersectionFromInside() {
        val world = World(pointLight = PointLight(point(0.0, 0.25, 0.0), color(1.0, 1.0, 1.0)))
        val ray = Ray(point(0.0, 0.0, 0.0), vector(0.0, 0.0, 1.0))
        val sphere = world.shapes[1]
        val intersection = Intersection(0.5, sphere)
        val computation = ShadingPreComputation.of(ray, intersection)
        assertEquals(color(0.90498, 0.90498, 0.90498), world.shadeHit(computation))
    }

    @Test
    fun testShadeHitWithIntersectionInShadow() {
        val world = World(
            pointLight = PointLight(point(0.0, 0.0, -10.0), color(1.0, 1.0, 1.0)),
            shapes = listOf(Sphere(), Sphere(transformationMatrix = Matrix.translation(0.0, 0.0, 10.0)))
        )
        val ray = Ray(point(0.0, 0.0, 5.0), vector(0.0, 0.0, 1.0))
        val intersection = Intersection(4.0, world.shapes[1])
        val computation = ShadingPreComputation.of(ray, intersection)
        assertEquals(color(0.1, 0.1, 0.1), world.shadeHit(computation))
    }

    @Test
    fun testColorWhenRayMisses() {
        val world = World()
        val ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 1.0, 0.0))
        assertEquals(color(0.0, 0.0, 0.0), world.colorAtIntersection(ray))
    }

    @Test
    fun testColorWhenRayHits() {
        val world = World()
        val ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        assertEquals(color(0.38066, 0.47583, 0.2855), world.colorAtIntersection(ray))
    }

    @Test
    fun testColorWhithIntersectionBehindRay() {
        val material = Material(
            surfaceColor = color(1.0, 1.0, 1.0),
            ambientReflection = 1.0,
            diffuseReflection = 0.9,
            specularReflection = 0.9,
            shininess = 200.0
        )
        val outerSphere = Sphere(Matrix.identity(4), material)
        val innerSphere = Sphere(Matrix.scaling(0.5, 0.5, 0.5), material)
        val world = World(shapes = listOf(outerSphere, innerSphere))
        val ray = Ray(point(0.0, 0.0, 0.75), vector(0.0, 0.0, -1.0))
        assertEquals(innerSphere.material.surfaceColor, world.colorAtIntersection(ray))
    }

    @Test
    fun testNoShadowWhenNothingIsCollinearWithPointAndLight() {
        val world = World()
        assertFalse(world.isShadowed(point(0.0, 10.0, 0.0)))
    }

    @Test
    fun testShadowWhenObjectIsBetweenPointAndLight() {
        val world = World()
        assertTrue(world.isShadowed(point(10.0, -10.0, 10.0)))
    }

    @Test
    fun testNoShadowWhenObjectIsBehindPoint() {
        val world = World()
        assertFalse(world.isShadowed(point(-2.0, 2.0, -2.0)))
    }

    @Test
    fun testIsShadowedMustBeCalledWithPoint() {
        assertThrows(IllegalArgumentException::class.java) { World().isShadowed(vector(1.0, 1.0, 1.0)) }
    }

    @Test
    fun testIntersectingDefaultWorld() {
        val world = World()
        val ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        val intersections = world.intersect(ray)
        assertEquals(4, intersections.count)
        assertEquals(4.0, intersections[0].time)
        assertEquals(4.5, intersections[1].time)
        assertEquals(5.5, intersections[2].time)
        assertEquals(6.0, intersections[3].time)
    }

    @Test
    fun testReflectedColorForNonreflectiveMaterial() {
        val world = World(
            shapes = listOf(
                Sphere(
                    material = Material(
                        surfaceColor = color(0.8, 1.0, 0.6),
                        ambientReflection = 0.1,
                        diffuseReflection = 0.7,
                        specularReflection = 0.2,
                        shininess = 200.0,
                        pattern = null,
                        reflective = 0.0
                    )
                ),
                Sphere(
                    Matrix.scaling(0.5, 0.5, 0.5),
                    Material(
                        surfaceColor = color(1.0, 1.0, 1.0),
                        ambientReflection = 1.0,
                        diffuseReflection = 0.9,
                        specularReflection = 0.9,
                        shininess = 200.0,
                        pattern = null,
                        reflective = 0.0
                    )
                )
            )
        )
        val ray = Ray(point(0.0, 0.0, 0.0), vector(0.0, 0.0, 1.0))
        val shape = world.shapes[1]
        val intersection = Intersection(1.0, shape)
        val computation = ShadingPreComputation.of(ray, intersection)
        assertEquals(color(0.0, 0.0, 0.0), world.reflectedColor(computation))
    }

    @Test
    fun testReflectedColorForReflectiveMaterial() {
        val world = World(
            shapes = listOf(
                Sphere(
                    transformationMatrix = Matrix.identity(4),
                    material = Material(
                        surfaceColor = color(0.8, 1.0, 0.6),
                        ambientReflection = 0.1,
                        diffuseReflection = 0.7,
                        specularReflection = 0.2,
                        shininess = 200.0,
                        pattern = null,
                        reflective = 0.0
                    )
                ),
                Sphere(
                    transformationMatrix = Matrix.scaling(0.5, 0.5, 0.5),
                    material = Material(
                        surfaceColor = color(1.0, 1.0, 1.0),
                        ambientReflection = 0.1,
                        diffuseReflection = 0.9,
                        specularReflection = 0.9,
                        shininess = 200.0,
                        pattern = null,
                        reflective = 0.0
                    )
                ),
                Plane(Matrix.translation(0.0, -1.0, 0.0), Material(reflective = 0.49998))
            )
        )
        val ray = Ray(point(0.0, 0.0, -3.0), vector(0.0, -sqrt(2.0) / 2, sqrt(2.0) / 2))
        val shape = world.shapes[2]
        val intersection = Intersection(sqrt(2.0), shape)
        val computation = ShadingPreComputation.of(ray, intersection)
        val reflectedColor = world.reflectedColor(computation)
        assertEquals(color(0.19032, 0.2379, 0.14274), reflectedColor)
    }

    @Test
    fun testShadeHitWithReflectiveMaterial() {
        val world = World(
            shapes = listOf(
                Sphere(
                    transformationMatrix = Matrix.identity(4),
                    material = Material(
                        surfaceColor = color(0.8, 1.0, 0.6),
                        ambientReflection = 0.1,
                        diffuseReflection = 0.7,
                        specularReflection = 0.2,
                        shininess = 200.0,
                        pattern = null,
                        reflective = 0.0
                    )
                ),
                Sphere(
                    transformationMatrix = Matrix.scaling(0.5, 0.5, 0.5),
                    material = Material(
                        surfaceColor = color(1.0, 1.0, 1.0),
                        ambientReflection = 0.1,
                        diffuseReflection = 0.9,
                        specularReflection = 0.9,
                        shininess = 200.0,
                        pattern = null,
                        reflective = 0.0
                    )
                ),
                Plane(Matrix.translation(0.0, -1.0, 0.0), Material(reflective = 0.50005))
            )
        )
        val ray = Ray(point(0.0, 0.0, -3.0), vector(0.0, -sqrt(2.0) / 2, sqrt(2.0) / 2))
        val shape = world.shapes[2]
        val intersection = Intersection(sqrt(2.0), shape)
        val computation = ShadingPreComputation.of(ray, intersection)
        val color = world.shadeHit(computation)
        assertEquals(color(0.87677, 0.92436, 0.82918), color)
    }

    @Test
    fun testColorAtIntersectionWithMutuallyReflectiveSurfaces() {
        val world = World(
            pointLight = PointLight(point(0.0, 0.0, 0.0), color(1.0, 1.0, 1.0)),
            shapes = listOf(
                Plane(Matrix.translation(0.0, -1.0, 0.0), Material(reflective = 1.0)),
                Plane(Matrix.translation(0.0, 1.0, 0.0), Material(reflective = 1.0))
            )
        )
        val ray = Ray(point(0.0, 0.0, 0.0), vector(0.0, 1.0, 0.0))
        assertDoesNotThrow { world.colorAtIntersection(ray) }
    }

    @Test
    fun testReflectedColorAtMaximumRecursionDepth() {
        val world = World(
            shapes = listOf(
                Sphere(
                    transformationMatrix = Matrix.identity(4),
                    material = Material(
                        surfaceColor = color(0.8, 1.0, 0.6),
                        ambientReflection = 0.1,
                        diffuseReflection = 0.7,
                        specularReflection = 0.2,
                        shininess = 200.0,
                        pattern = null,
                        reflective = 0.0
                    )
                ),
                Sphere(
                    transformationMatrix = Matrix.scaling(0.5, 0.5, 0.5),
                    material = Material(
                        surfaceColor = color(1.0, 1.0, 1.0),
                        ambientReflection = 0.1,
                        diffuseReflection = 0.9,
                        specularReflection = 0.9,
                        shininess = 200.0,
                        pattern = null,
                        reflective = 0.0
                    )
                ),
                Plane(Matrix.translation(0.0, -1.0, 0.0), Material(reflective = 0.5))
            )
        )
        val ray = Ray(point(0.0, 0.0, -3.0), vector(0.0, -sqrt(2.0) / 2, sqrt(2.0) / 2))
        val shape = world.shapes[2]
        val intersection = Intersection(sqrt(2.0), shape)
        val computation = ShadingPreComputation.of(ray, intersection)
        val color = world.reflectedColor(computation, 0)
        assertEquals(color(0.0, 0.0, 0.0), color)
    }

    @Test
    fun testRefractedColorWithOpaqueSurface() {
        val world = World()
        val shape = world.shapes[0]
        val ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        val intersections = Intersections(Intersection(4.0, shape), Intersection(6.0, shape))
        val computation = ShadingPreComputation.of(ray, intersections[0], intersections)
        assertEquals(color(0.0, 0.0, 0.0), world.refractedColor(computation, 5))
    }

    @Test
    fun testRefractedColorAtMaximumRecursionDepth() {
        val world = World(
            shapes = listOf(
                Sphere(
                    transformationMatrix = Matrix.identity(4),
                    material = Material(
                        surfaceColor = color(0.8, 1.0, 0.6),
                        ambientReflection = 0.1,
                        diffuseReflection = 0.7,
                        specularReflection = 0.2,
                        shininess = 200.0,
                        pattern = null,
                        reflective = 0.0,
                        transparency = 1.0,
                        refractiveIndex = 1.5
                    )
                ),
                Sphere(
                    transformationMatrix = Matrix.scaling(0.5, 0.5, 0.5),
                    material = Material(
                        surfaceColor = color(1.0, 1.0, 1.0),
                        ambientReflection = 0.1,
                        diffuseReflection = 0.9,
                        specularReflection = 0.9,
                        shininess = 200.0,
                        pattern = null,
                        reflective = 0.0
                    )
                )
            )
        )
        val ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        val shape = world.shapes[0]
        val intersections = Intersections(Intersection(4.0, shape), Intersection(6.0, shape))
        val computation = ShadingPreComputation.of(ray, intersections[0], intersections)
        val color = world.refractedColor(computation, 0)
        assertEquals(color(0.0, 0.0, 0.0), color)
    }

    @Test
    fun testRefractedColorUnderTotalInternalReflection() {
        val world = World(
            shapes = listOf(
                Sphere(
                    transformationMatrix = Matrix.identity(4),
                    material = Material(
                        surfaceColor = color(0.8, 1.0, 0.6),
                        ambientReflection = 0.1,
                        diffuseReflection = 0.7,
                        specularReflection = 0.2,
                        shininess = 200.0,
                        pattern = null,
                        reflective = 0.0,
                        transparency = 1.0,
                        refractiveIndex = 1.5
                    )
                ),
                Sphere(
                    transformationMatrix = Matrix.scaling(0.5, 0.5, 0.5),
                    material = Material(
                        surfaceColor = color(1.0, 1.0, 1.0),
                        ambientReflection = 0.1,
                        diffuseReflection = 0.9,
                        specularReflection = 0.9,
                        shininess = 200.0,
                        pattern = null,
                        reflective = 0.0
                    )
                )
            )
        )
        val ray = Ray(point(0.0, 0.0, sqrt(2.0) / 2), vector(0.0, 1.0, 0.0))
        val shape = world.shapes[0]
        val intersections = Intersections(Intersection(-sqrt(2.0) / 2, shape), Intersection(sqrt(2.0) / 2, shape))
        val computation = ShadingPreComputation.of(ray, intersections[1], intersections)
        val color = world.refractedColor(computation, 5)
        assertEquals(color(0.0, 0.0, 0.0), color)
    }

    @Test
    fun testRefractedColorWithRefractedRay() {
        val world = World(
            shapes = listOf(
                Sphere(
                    transformationMatrix = Matrix.identity(4),
                    material = Material(
                        surfaceColor = color(0.8, 1.0, 0.6),
                        ambientReflection = 1.0,
                        diffuseReflection = 0.7,
                        specularReflection = 0.2,
                        shininess = 200.0,
                        pattern = createPattern(),
                        reflective = 0.0
                    )
                ),
                Sphere(
                    transformationMatrix = Matrix.scaling(0.5, 0.5, 0.5),
                    material = Material(
                        surfaceColor = color(1.0, 1.0, 1.0),
                        ambientReflection = 0.1,
                        diffuseReflection = 0.9,
                        specularReflection = 0.9,
                        shininess = 200.0,
                        pattern = null,
                        reflective = 0.0,
                        transparency = 1.0,
                        refractiveIndex = 1.4998
                    )
                )
            )
        )
        val ray = Ray(point(0.0, 0.0, 0.1), vector(0.0, 1.0, 0.0))
        val intersections = Intersections(
            Intersection(-0.9899, world.shapes[0]),
            Intersection(-0.4899, world.shapes[1]),
            Intersection(0.4899, world.shapes[1]),
            Intersection(0.9899, world.shapes[0])
        )
        val computation = ShadingPreComputation.of(ray, intersections[2], intersections)
        val color = world.refractedColor(computation, 5)
        assertEquals(color(0.0, 0.99888, 0.04725), color)
    }

    @Test
    fun testShadeHitWithTransparentMaterial() {
        val floor = Plane(Matrix.translation(0.0, -1.0, 0.0), Material(transparency = 0.5, refractiveIndex = 1.5))
        val ball = Sphere(
            Matrix.translation(0.0, -3.5, -0.5),
            Material(surfaceColor = color(1.0, 0.0, 0.0), ambientReflection = 0.5)
        )
        val world = World(
            shapes = listOf(
                Sphere(
                    transformationMatrix = Matrix.identity(4),
                    material = Material(
                        surfaceColor = color(0.8, 1.0, 0.6),
                        ambientReflection = 0.1,
                        diffuseReflection = 0.7,
                        specularReflection = 0.2,
                        shininess = 200.0,
                        pattern = null,
                        reflective = 0.0
                    )
                ),
                Sphere(
                    transformationMatrix = Matrix.scaling(0.5, 0.5, 0.5),
                    material = Material(
                        surfaceColor = color(1.0, 1.0, 1.0),
                        ambientReflection = 0.1,
                        diffuseReflection = 0.9,
                        specularReflection = 0.9,
                        shininess = 200.0,
                        pattern = null,
                        reflective = 0.0
                    )
                ), floor, ball
            )
        )
        val ray = Ray(point(0.0, 0.0, -3.0), vector(0.0, -sqrt(2.0) / 2, sqrt(2.0) / 2))
        val intersections = Intersections(Intersection(sqrt(2.0), floor))
        val computation = ShadingPreComputation.of(ray, intersections[0], intersections)
        val color = world.shadeHit(computation, 5)
        assertEquals(color(0.93642, 0.68642, 0.68642), color)
    }

    @Test
    fun testShadeHitWithReflectiveTransparentMaterial() {
        val floor = Plane(
            Matrix.translation(0.0, -1.0, 0.0),
            Material(reflective = 0.5, transparency = 0.5, refractiveIndex = 1.5)
        )
        val ball = Sphere(
            Matrix.translation(0.0, -3.5, -0.5),
            Material(surfaceColor = color(1.0, 0.0, 0.0), ambientReflection = 0.5)
        )
        val world = World(
            shapes = listOf(
                Sphere(
                    transformationMatrix = Matrix.identity(4),
                    material = Material(
                        surfaceColor = color(0.8, 1.0, 0.6),
                        ambientReflection = 0.1,
                        diffuseReflection = 0.7,
                        specularReflection = 0.2,
                        shininess = 200.0,
                        pattern = null,
                        reflective = 0.0
                    )
                ),
                Sphere(
                    transformationMatrix = Matrix.scaling(0.5, 0.5, 0.5),
                    material = Material(
                        surfaceColor = color(1.0, 1.0, 1.0),
                        ambientReflection = 0.1,
                        diffuseReflection = 0.9,
                        specularReflection = 0.9,
                        shininess = 200.0,
                        pattern = null,
                        reflective = 0.0
                    )
                ), floor, ball
            )
        )
        val ray = Ray(point(0.0, 0.0, -3.0), vector(0.0, -sqrt(2.0) / 2, sqrt(2.0) / 2))
        val intersections = Intersections(Intersection(sqrt(2.0), floor))
        val computation = ShadingPreComputation.of(ray, intersections[0], intersections)
        val color = world.shadeHit(computation, 5)
        assertEquals(color(0.93391, 0.69643, 0.69243), color)
    }

    private fun createPattern() =
        object : Pattern(Matrix.identity(4)) {
            override fun patternAt(point: Tuple) = Tuple(point[0], point[1], point[2])
        }

}
