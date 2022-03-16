package raytracer.elements

import raytracer.elements.Tuple.Companion.vector
import kotlin.math.abs

/**
 * The cube is centered at the origin and extends from -1 to 1 along each axis.
 * It can be transformed by providing a [transformationMatrix].
 */
class Cube(
    transformationMatrix: Matrix = Matrix.identity(4),
    material: Material = Material()
) : Shape(transformationMatrix, material) {

    override fun localIntersect(ray: Ray): Intersections {
        val (xtmin, xtmax) = checkAxis(ray.origin[0], ray.direction[0])
        val (ytmin, ytmax) = checkAxis(ray.origin[1], ray.direction[1])
        val (ztmin, ztmax) = checkAxis(ray.origin[2], ray.direction[2])

        val tmin = maxOf(xtmin, ytmin, ztmin)
        val tmax = minOf(xtmax, ytmax, ztmax)

        return if (tmin > tmax) Intersections()
        else Intersections(Intersection(tmin, this), Intersection(tmax, this))
    }

    private fun checkAxis(origin: Double, direction: Double): Pair<Double, Double> {
        val tminNumerator = -1 - origin
        val tmaxNumerator = 1 - origin
        val tmin: Double = tminNumerator / direction
        val tmax: Double = tmaxNumerator / direction
        return Pair(tmin, tmax).sorted()
    }

    override fun localNormalAt(point: Tuple): Tuple {
        val (x, y, z) = point
        return when (maxOf(abs(x), abs(y), abs(z))) {
            abs(x) -> vector(x, 0.0, 0.0)
            abs(y) -> vector(0.0, y, 0.0)
            else -> vector(0.0, 0.0, z)
        }
    }

}
