package raytracer.elements

import raytracer.elements.Tuple.Companion.point
import raytracer.elements.Tuple.Companion.vector
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * The double-napped cone is centered at the origin.
 *
 * It is infinitely long by default but it can be truncated with [minimum] and
 * [maximum]. Both values are outside of the bounds of the cone.
 *
 * It can be open or [closed].
 *
 * It can be transformed by providing a [transformationMatrix].
 */
class Cone(
    transformationMatrix: Matrix = Matrix.identity(4),
    material: Material = Material(),
    private val minimum: Double = Double.NEGATIVE_INFINITY,
    private val maximum: Double = Double.POSITIVE_INFINITY,
    private val closed: Boolean = false
) : Shape(transformationMatrix, material) {

    private val minimumSquare = minimum.pow(2)
    private val maximumSquare = maximum.pow(2)

    init {
        if (minimum == Double.NEGATIVE_INFINITY || maximum == Double.POSITIVE_INFINITY)
            require(!closed) { "infinite cones cannot be closed" }
    }

    override fun localIntersect(ray: Ray): Intersections {
        val intersections = mutableListOf<Intersection>()

        val a = ray.direction[0].pow(2) - ray.direction[1].pow(2) + ray.direction[2].pow(2)
        val b =
            2 * (ray.origin[0] * ray.direction[0] - ray.origin[1] * ray.direction[1] + ray.origin[2] * ray.direction[2])
        val c = ray.origin[0].pow(2) - ray.origin[1].pow(2) + ray.origin[2].pow(2)

        if (abs(a) < EPSILON && abs(b) > EPSILON)
            intersections.add(Intersection(-c / (2 * b), this))

        if (abs(a) > EPSILON) {
            val discriminant = b.pow(2) - 4 * a * c
            if (discriminant >= 0) {
                val t0 = (-b - sqrt(discriminant)) / (2 * a)
                val y0 = ray.origin[1] + t0 * ray.direction[1]
                if (minimum < y0 && y0 < maximum) intersections.add(Intersection(t0, this))
                val t1 = (-b + sqrt(discriminant)) / (2 * a)
                val y1 = ray.origin[1] + t1 * ray.direction[1]
                if (minimum < y1 && y1 < maximum) intersections.add(Intersection(t1, this))
            }
        }

        return Intersections(*intersections.toTypedArray()).accumulate(intersectCaps(ray))
    }

    private fun intersectCaps(ray: Ray): Intersections {
        if (!closed || ray.direction[1] == 0.0) return Intersections()

        val t0 = (minimum - ray.origin[1]) / ray.direction[1]
        val t1 = (maximum - ray.origin[1]) / ray.direction[1]

        val intersections1 =
            if (checkCap(ray, t0, minimumSquare)) Intersections(Intersection(t0, this))
            else Intersections()

        val intersections2 =
            if (checkCap(ray, t1, maximumSquare)) Intersections(Intersection(t1, this))
            else Intersections()

        return intersections1.accumulate(intersections2)
    }

    private fun checkCap(ray: Ray, time: Double, radius: Double): Boolean {
        val x = ray.origin[0] + time * ray.direction[0]
        val z = ray.origin[2] + time * ray.direction[2]
        return x.pow(2) + z.pow(2) <= radius
    }

    override fun localNormalAt(point: Tuple, hit: Intersection?): Tuple {
        val (x, y, z) = point
        val distanceFromYAxis = x.pow(2) + z.pow(2)
        return when {
            distanceFromYAxis < maximumSquare && y >= maximum - EPSILON -> vector(0.0, 1.0, 0.0)
            distanceFromYAxis < minimumSquare && y <= minimum + EPSILON -> vector(0.0, -1.0, 0.0)
            else -> {
                val s = sqrt(distanceFromYAxis)
                vector(x, if (y > 0) -s else s, z)
            }
        }
    }

    override fun localBoundingBox(): BoundingBox {
        val extent = max(abs(minimum), abs(maximum))
        return BoundingBox(point(-extent, minimum, -extent), point(extent, maximum, extent))
    }

}
