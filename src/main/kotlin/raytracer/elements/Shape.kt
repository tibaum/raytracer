package raytracer.elements

abstract class Shape(
    val transformationMatrix: Matrix = Matrix.identity(4),
    val material: Material = Material(
        surfaceColor = Tuple.color(1.0, 1.0, 1.0),
        ambientReflection = 0.1,
        diffuseReflection = 0.9,
        specularReflection = 0.9,
        shininess = 200.0
    )
) {

    private val inverseTransformation: Matrix = transformationMatrix.inverse()
    private val transposedInverseTransformation: Matrix = inverseTransformation.transpose()

    val center = Tuple.point(0.0, 0.0, 0.0)

    /**
     * Computes the distance between the origin of the ray and its intersection points with the sphere.
     */
    fun intersect(ray: Ray): Intersections {
        val localRay = relateRayToUnitShape(ray)
        return localIntersect(localRay)
    }

    abstract fun localIntersect(ray: Ray): Intersections

    /**
     * Moves the ray such that the relationship between the original ray and
     * the original shape is equal to the relationship between the new ray
     * and the unit shape.
     */
    fun relateRayToUnitShape(ray: Ray): Ray =
        Ray(inverseTransformation * ray.origin, inverseTransformation * ray.direction)

    /**
     * Computes the surface normal, i.e., a vector that is perpendicular to the surface of the sphere.
     */
    fun normalAt(point: Tuple): Tuple {
        val localPoint = inverseTransformation * point
        val localNormal = localNormalAt(localPoint)
        val worldNormal = transposedInverseTransformation * localNormal
        return worldNormal.asVector().normalize()
    }

    abstract fun localNormalAt(point: Tuple): Tuple

}