package raytracer.elements

import kotlin.math.max
import kotlin.math.min

class BoundingBox(val min: Tuple, val max: Tuple) {

    fun accumulate(other: BoundingBox): BoundingBox {
        val minimum = min.elementWise(::min, other.min)
        val maximum = max.elementWise(::max, other.max)
        return BoundingBox(minimum, maximum)
    }

    fun intersect(ray: Ray): Boolean {
        val (xtmin, xtmax) = checkAxis(ray.origin[0], ray.direction[0], min[0], max[0])
        val (ytmin, ytmax) = checkAxis(ray.origin[1], ray.direction[1], min[1], max[1])
        val (ztmin, ztmax) = checkAxis(ray.origin[2], ray.direction[2], min[2], max[2])

        val tmin = maxOf(xtmin, ytmin, ztmin)
        val tmax = minOf(xtmax, ytmax, ztmax)

        return tmin <= tmax
    }

    private fun checkAxis(origin: Double, direction: Double, min: Double, max: Double): Pair<Double, Double> {
        val tminNumerator = min - origin
        val tmaxNumerator = max - origin
        val tmin: Double = tminNumerator / direction
        val tmax: Double = tmaxNumerator / direction
        return Pair(tmin, tmax).sorted()
    }

}
