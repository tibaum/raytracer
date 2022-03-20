package raytracer.elements

import raytracer.elements.Tuple.Companion.vector
import kotlin.Double.Companion.NEGATIVE_INFINITY
import kotlin.Double.Companion.POSITIVE_INFINITY
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * The cylinder is centered at the origin and its radius is 1 along each axis.
 *
 * It is infinitely long by default but it can be truncated with [minimum] and
 * [maximum]. Both values are outside of the bounds of the cylinder.
 *
 * It can be open or [closed].
 *
 * It can be transformed by providing a [transformationMatrix].
 */
class Cylinder(
    transformationMatrix: Matrix = Matrix.identity(4),
    material: Material = Material(),
    private val minimum: Double = NEGATIVE_INFINITY,
    private val maximum: Double = POSITIVE_INFINITY,
    private val closed: Boolean = false
) : Shape(transformationMatrix, material) {

    init {
        if (minimum == NEGATIVE_INFINITY || maximum == POSITIVE_INFINITY)
            require(!closed) { "infinite cylinders cannot be closed" }
    }

    override fun localIntersect(ray: Ray): Intersections {
        val a = ray.direction[0].pow(2) + ray.direction[2].pow(2)

        if (abs(a) < EPSILON) return intersectCaps(ray)

        val b = 2 * ray.origin[0] * ray.direction[0] + 2 * ray.origin[2] * ray.direction[2]
        val c = ray.origin[0].pow(2) + ray.origin[2].pow(2) - 1
        val discriminant = b.pow(2) - 4 * a * c

        if (discriminant < 0) return Intersections()

        val t0 = (-b - sqrt(discriminant)) / (2 * a)
        val y0 = ray.origin[1] + t0 * ray.direction[1]
        val t1 = (-b + sqrt(discriminant)) / (2 * a)
        val y1 = ray.origin[1] + t1 * ray.direction[1]

        val intersections1 =
            if (minimum < y0 && y0 < maximum) Intersections(Intersection(t0, this))
            else Intersections()

        val intersections2 =
            if (minimum < y1 && y1 < maximum) Intersections(Intersection(t1, this))
            else Intersections()

        val intersections3 = intersectCaps(ray)

        return intersections1.accumulate(intersections2).accumulate(intersections3)
    }

    private fun intersectCaps(ray: Ray): Intersections {
        if (!closed || ray.direction[1] == 0.0) return Intersections()

        val t0 = (minimum - ray.origin[1]) / ray.direction[1]
        val t1 = (maximum - ray.origin[1]) / ray.direction[1]

        val intersections1 =
            if (checkCap(ray, t0)) Intersections(Intersection(t0, this))
            else Intersections()

        val intersections2 =
            if (checkCap(ray, t1)) Intersections(Intersection(t1, this))
            else Intersections()

        return intersections1.accumulate(intersections2)
    }

    private fun checkCap(ray: Ray, time: Double): Boolean {
        val x = ray.origin[0] + time * ray.direction[0]
        val z = ray.origin[2] + time * ray.direction[2]
        return x.pow(2) + z.pow(2) <= 1
    }

    override fun localNormalAt(point: Tuple): Tuple {
        val (x, y, z) = point
        val distanceFromYAxis = x.pow(2) + z.pow(2)
        return when {
            distanceFromYAxis < 1 && y >= maximum - EPSILON -> vector(0.0, 1.0, 0.0)
            distanceFromYAxis < 1 && y <= minimum + EPSILON -> vector(0.0, -1.0, 0.0)
            else -> vector(x, 0.0, z)
        }
    }

}
