package raytracer.elements

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import raytracer.elements.Tuple.Companion.point
import raytracer.elements.Tuple.Companion.vector
import kotlin.math.abs
import kotlin.math.sqrt

class ShadingPreComputationTest {

    @Test
    fun testPrecomputingTheStateOfAnIntersectionOutside() {
        val ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        val sphere = Sphere()
        val intersection = Intersection(4.0, sphere)
        val intersectionData = ShadingPreComputation.of(ray, intersection)
        assertEquals(intersection.time, intersectionData.hitTime)
        assertEquals(intersection.shape, intersectionData.shape)
        assertEquals(point(0.0, 0.0, -1.0), intersectionData.intersectionPoint)
        assertEquals(vector(0.0, 0.0, -1.0), intersectionData.eyeVector)
        assertEquals(vector(0.0, 0.0, -1.0), intersectionData.normalVector)
        assertEquals(false, intersectionData.intersectionOccursInside)
    }

    @Test
    fun testPrecomputingTheStateOfAnIntersectionInside() {
        val ray = Ray(point(0.0, 0.0, 0.0), vector(0.0, 0.0, 1.0))
        val sphere = Sphere()
        val intersection = Intersection(1.0, sphere)
        val intersectionData = ShadingPreComputation.of(ray, intersection)
        assertEquals(intersection.time, intersectionData.hitTime)
        assertEquals(intersection.shape, intersectionData.shape)
        assertEquals(point(0.0, 0.0, 1.0), intersectionData.intersectionPoint)
        assertEquals(vector(0.0, 0.0, -1.0), intersectionData.eyeVector)
        assertEquals(vector(0.0, 0.0, -1.0), intersectionData.normalVector)
        assertEquals(true, intersectionData.intersectionOccursInside)
    }

    @Test
    fun testCreationWithIllegalArguments() {
        assertThrows(IllegalArgumentException::class.java) {
            ShadingPreComputation(
                0.1,
                Sphere(),
                vector(1.0, 1.0, 1.0),
                point(1.0, 1.0, 1.0),
                vector(1.0, 1.0, 1.0),
                vector(1.0, 1.0, 1.0),
                false,
                vector(1.0, 1.0, 1.0),
                1.0,
                1.0,
                point(1.0, 1.0, 1.0)
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            ShadingPreComputation(
                0.1,
                Sphere(),
                point(1.0, 1.0, 1.0),
                vector(1.0, 1.0, 1.0),
                vector(1.0, 1.0, 1.0),
                vector(1.0, 1.0, 1.0),
                false,
                vector(1.0, 1.0, 1.0),
                1.0,
                1.0,
                point(1.0, 1.0, 1.0)
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            ShadingPreComputation(
                0.1,
                Sphere(),
                point(1.0, 1.0, 1.0),
                point(1.0, 1.0, 1.0),
                point(1.0, 1.0, 1.0),
                vector(1.0, 1.0, 1.0),
                false,
                vector(1.0, 1.0, 1.0),
                1.0,
                1.0,
                point(1.0, 1.0, 1.0)
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            ShadingPreComputation(
                0.1,
                Sphere(),
                point(1.0, 1.0, 1.0),
                point(1.0, 1.0, 1.0),
                vector(1.0, 1.0, 1.0),
                point(1.0, 1.0, 1.0),
                false,
                vector(1.0, 1.0, 1.0),
                1.0,
                1.0,
                point(1.0, 1.0, 1.0)
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            ShadingPreComputation(
                0.1,
                Sphere(),
                point(1.0, 1.0, 1.0),
                point(1.0, 1.0, 1.0),
                vector(1.0, 1.0, 1.0),
                vector(1.0, 1.0, 1.0),
                false,
                point(1.0, 1.0, 1.0),
                1.0,
                1.0,
                point(1.0, 1.0, 1.0)
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            ShadingPreComputation(
                0.1,
                Sphere(),
                point(1.0, 1.0, 1.0),
                point(1.0, 1.0, 1.0),
                vector(1.0, 1.0, 1.0),
                vector(1.0, 1.0, 1.0),
                false,
                vector(1.0, 1.0, 1.0),
                -0.1,
                1.0,
                point(1.0, 1.0, 1.0)
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            ShadingPreComputation(
                0.1,
                Sphere(),
                point(1.0, 1.0, 1.0),
                point(1.0, 1.0, 1.0),
                vector(1.0, 1.0, 1.0),
                vector(1.0, 1.0, 1.0),
                false,
                vector(1.0, 1.0, 1.0),
                1.0,
                -0.1,
                point(1.0, 1.0, 1.0)
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            ShadingPreComputation(
                0.1,
                Sphere(),
                point(1.0, 1.0, 1.0),
                point(1.0, 1.0, 1.0),
                vector(1.0, 1.0, 1.0),
                vector(1.0, 1.0, 1.0),
                false,
                vector(1.0, 1.0, 1.0),
                1.0,
                1.0,
                vector(1.0, 1.0, 1.0)
            )
        }
    }

    @Test
    fun testHitShouldOffsetPoint() {
        val ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        val sphere = Sphere(transformationMatrix = Matrix.translation(0.0, 0.0, 1.0))
        val intersection = Intersection(5.0, sphere)
        val computation = ShadingPreComputation.of(ray, intersection)
        assertTrue(computation.overPoint[2] < -EPSILON / 2)
        assertTrue(computation.intersectionPoint[2] > computation.overPoint[2])
    }

    @Test
    fun testPreComputeReflectionVector() {
        val ray = Ray(point(0.0, 1.0, -1.0), vector(0.0, -sqrt(2.0) / 2, sqrt(2.0) / 2))
        val plane = Plane()
        val intersection = Intersection(sqrt(2.0), plane)
        val computation = ShadingPreComputation.of(ray, intersection)
        assertEquals(vector(0.0, sqrt(2.0) / 2, sqrt(2.0) / 2), computation.reflectVector)
    }

    @Test
    fun testFindingN1andN2AtVariousIntersections() {
        val sphereA = Sphere(Matrix.scaling(2.0, 2.0, 2.0), Material(transparency = 1.0, refractiveIndex = 1.5))
        val sphereB = Sphere(Matrix.translation(0.0, 0.0, -0.25), Material(transparency = 1.0, refractiveIndex = 2.0))
        val sphereC = Sphere(Matrix.translation(0.0, 0.0, 0.25), Material(transparency = 1.0, refractiveIndex = 2.5))
        val ray = Ray(point(0.0, 0.0, -4.0), vector(0.0, 0.0, 1.0))
        val intersections = Intersections(
            Intersection(2.0, sphereA),
            Intersection(2.75, sphereB),
            Intersection(3.25, sphereC),
            Intersection(4.75, sphereB),
            Intersection(5.25, sphereC),
            Intersection(6.0, sphereA)
        )

        val computation0 = ShadingPreComputation.of(ray, intersections[0], intersections)
        assertEquals(1.0, computation0.refractiveIndexOfMaterialExited)
        assertEquals(1.5, computation0.refractiveIndexOfMaterialEntered)

        val computation1 = ShadingPreComputation.of(ray, intersections[1], intersections)
        assertEquals(1.5, computation1.refractiveIndexOfMaterialExited)
        assertEquals(2.0, computation1.refractiveIndexOfMaterialEntered)

        val computation2 = ShadingPreComputation.of(ray, intersections[2], intersections)
        assertEquals(2.0, computation2.refractiveIndexOfMaterialExited)
        assertEquals(2.5, computation2.refractiveIndexOfMaterialEntered)

        val computation3 = ShadingPreComputation.of(ray, intersections[3], intersections)
        assertEquals(2.5, computation3.refractiveIndexOfMaterialExited)
        assertEquals(2.5, computation3.refractiveIndexOfMaterialEntered)

        val computation4 = ShadingPreComputation.of(ray, intersections[4], intersections)
        assertEquals(2.5, computation4.refractiveIndexOfMaterialExited)
        assertEquals(1.5, computation4.refractiveIndexOfMaterialEntered)

        val computation5 = ShadingPreComputation.of(ray, intersections[5], intersections)
        assertEquals(1.5, computation5.refractiveIndexOfMaterialExited)
        assertEquals(1.0, computation5.refractiveIndexOfMaterialEntered)
    }

    @Test
    fun testUnderpointIsOffsetBelowSurface() {
        val sphere = Sphere(Matrix.translation(0.0, 0.0, 1.0), Material(transparency = 1.0, refractiveIndex = 1.5))
        val ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        val intersections = Intersections(Intersection(5.0, sphere))
        val computation = ShadingPreComputation.of(ray, intersections[0], intersections)
        assertTrue(computation.underPoint[2] > EPSILON / 2)
        assertTrue(computation.underPoint[2] > computation.intersectionPoint[2])
    }

    @Test
    fun testSchlickApproximationUnderTotalReflection() {
        val shape = Sphere.glass()
        val ray = Ray(point(0.0, 0.0, sqrt(2.0) / 2), vector(0.0, 1.0, 0.0))
        val intersections = Intersections(
            Intersection(-sqrt(2.0) / 2, shape),
            Intersection(sqrt(2.0) / 2, shape)
        )
        val computation = ShadingPreComputation.of(ray, intersections[1], intersections)
        val reflectance = computation.schlick()
        assertEquals(1.0, reflectance)
    }

    @Test
    fun testSchlickApproximationWithPerpendiculaViewingAngle() {
        val shape = Sphere.glass()
        val ray = Ray(point(0.0, 0.0, 0.0), vector(0.0, 1.0, 0.0))
        val intersections = Intersections(
            Intersection(-1.0, shape),
            Intersection(1.0, shape)
        )
        val computation = ShadingPreComputation.of(ray, intersections[1], intersections)
        val reflectance = computation.schlick()
        assertTrue(abs(0.04 - reflectance) < EPSILON)
    }

    @Test
    fun testSchlickApproximationWithSmallViewingAngle() {
        val shape = Sphere.glass()
        val ray = Ray(point(0.0, 0.99, -2.0), vector(0.0, 0.0, 1.0))
        val intersections = Intersections(Intersection(1.8589, shape))
        val computation = ShadingPreComputation.of(ray, intersections[0], intersections)
        val reflectance = computation.schlick()
        assertTrue(abs(0.48873 - reflectance) < EPSILON)
    }

    @Test
    fun testPreparingNormalOnSmoothTriangle() {
        val triangle = SmoothTriangle(
            point1 = point(0.0, 1.0, 0.0),
            point2 = point(-1.0, 0.0, 0.0),
            point3 = point(1.0, 0.0, 0.0),
            normal1 = vector(0.0, 1.0, 0.0),
            normal2 = vector(-1.0, 0.0, 0.0),
            normal3 = vector(1.0, 0.0, 0.0)
        )
        val hit = Intersection(1.0, triangle, 0.45, 0.25)
        val ray = Ray(point(-0.2, 0.3, -2.0), vector(0.0, 0.0, 1.0))
        val intersections = Intersections(hit)
        val computation = ShadingPreComputation.of(ray, hit, intersections)
        assertEquals(vector(-0.5547, 0.83205, 0.0), computation.normalVector)
    }

}
