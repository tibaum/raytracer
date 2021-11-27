package raytracer.elements

class Sphere(val center: Tuple) {

    companion object {
        /**
         * Creates a sphere centered at the origin
         */
        fun unit() = Sphere(Tuple.point(0.0, 0.0, 0.0))
    }

}
