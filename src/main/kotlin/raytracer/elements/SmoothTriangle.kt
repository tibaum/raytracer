package raytracer.elements

class SmoothTriangle(
    transformationMatrix: Matrix = Matrix.identity(4),
    material: Material = Material(),
    point1: Tuple,
    point2: Tuple,
    point3: Tuple,
    val normal1: Tuple,
    val normal2: Tuple,
    val normal3: Tuple
) : Triangle(transformationMatrix, material, point1, point2, point3) {

    init {
        require(normal1.isVector()) { "normal1 must be a vector but was: $normal1" }
        require(normal2.isVector()) { "normal2 must be a vector but was: $normal2" }
        require(normal3.isVector()) { "normal3 must be a vector but was: $normal3" }
    }

    override fun localNormalAt(point: Tuple, hit: Intersection?): Tuple {
        require(hit != null && hit.u != null && hit.v != null) { "hit.u and hit.v must be non-null" }
        return normal2 * hit.u + normal3 * hit.v + normal1 * (1 - hit.u - hit.v)
    }

}
