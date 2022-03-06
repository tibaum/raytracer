package raytracer.elements

data class Ray(val origin: Tuple, val direction: Tuple) {

    init {
        require(origin.isPoint()) { "origin must be a point" }
        require(direction.isVector()) { "direction must be a vector" }
    }

    /**
     * Computes the point at the given distance along the ray.
     */
    fun position(distance: Double): Tuple = origin + direction * distance

}
