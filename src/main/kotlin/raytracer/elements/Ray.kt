package raytracer.elements

import kotlin.math.pow
import kotlin.math.sqrt

class Ray(val origin: Tuple, val direction: Tuple) {

    init {
        if (!origin.isPoint())
            throw IllegalArgumentException("origin must be a point")
        if (!direction.isVector())
            throw IllegalArgumentException("direction must be a vector")
    }

    /**
     * Computes the point at the given distance along the ray.
     */
    fun position(distance: Double): Tuple = origin + direction * distance

    /**
     * Computes the distance between the origin of the ray and its intersection points with the sphere.
     */
    fun intersect(sphere: Sphere): List<Double> {
        val sphereToRay = origin - sphere.center
        val a = direction.dot(direction)
        val b = 2 * direction.dot(sphereToRay)
        val c = sphereToRay.dot(sphereToRay) - 1
        val discriminant = b.pow(2) - 4 * a * c
        if (discriminant < 0)
            return emptyList()
        val t1 = (-b - sqrt(discriminant)) / (2 * a)
        val t2 = (-b + sqrt(discriminant)) / (2 * a)
        return listOf(t1, t2)
    }

}
