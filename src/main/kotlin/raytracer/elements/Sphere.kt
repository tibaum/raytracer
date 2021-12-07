package raytracer.elements

/**
 * This sphere is represented as the unit sphere, i.e., it has its center at the
 * origin of the coordinate system and it has a radius of 1, transformed by a
 * transformation matrix. This way, rays cast at the sphere can be transformed
 * by the inverse of the transformation matrix and cast at the unit sphere.
 */
class Sphere(val transformationMatrix: Matrix = Matrix.identity(4)) {

    val center = Tuple.point(0.0, 0.0, 0.0)
    val radius = 1.0

    /**
     * Computes the surface normal, i.e., a vector that is perpendicular to the surface of the sphere.
     */
    fun normalAt(worldPoint: Tuple): Tuple {
        val inverseTransformation = transformationMatrix.inverse()
        val objectPoint = inverseTransformation * worldPoint
        val objectNormal = objectPoint - center
        val worldNormal = inverseTransformation.transpose() * objectNormal
        return worldNormal.asVector().normalize()
    }

}
