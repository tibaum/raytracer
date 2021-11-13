package raytracer.elements

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

}
