package raytracer.elements

import kotlin.math.pow

abstract class Shape(
    val transformationMatrix: Matrix,
    val material: Material
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

    /**
     * Moves the ray such that the relationship between the original ray and
     * the original shape is equal to the relationship between the new ray
     * and the unit shape.
     */
    fun relateRayToUnitShape(ray: Ray): Ray =
        Ray(inverseTransformation * ray.origin, inverseTransformation * ray.direction)

    abstract fun localIntersect(ray: Ray): Intersections

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

    /**
     * Calculates the shade which makes the object appear three-dimensional.
     */
    fun lightning(
        pointLight: PointLight,
        illuminatedPoint: Tuple,
        eyeVector: Tuple,
        normalVector: Tuple,
        inShadow: Boolean
    ): Tuple {
        val effectiveColor = material.surfaceColor.hadamardProduct(pointLight.intensity)
        val lightVector = (pointLight.position - illuminatedPoint).normalize()
        val lightDotNormal = lightVector.dot(normalVector)

        val ambientContribution = effectiveColor * material.ambientReflection

        val diffuseContribution =
            if (inShadow || lightDotNormal < 0) Tuple.black
            else effectiveColor * material.diffuseReflection * lightDotNormal

        val specularContribution = if (inShadow || lightDotNormal < 0) Tuple.black else {
            val reflectVector = (-lightVector).reflectAround(normalVector)
            val reflectDotEye = reflectVector.dot(eyeVector)
            if (reflectDotEye <= 0) Tuple.black
            else pointLight.intensity * material.specularReflection * reflectDotEye.pow(material.shininess)
        }

        return ambientContribution + diffuseContribution + specularContribution
    }

}
