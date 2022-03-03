package raytracer.elements

import kotlin.math.pow

/**
 * This sphere is represented as the unit sphere, i.e., it has its center at the
 * origin of the coordinate system and it has a radius of 1, transformed by a
 * transformation matrix. This way, rays cast at the sphere can be transformed
 * by the inverse of the transformation matrix and cast at the unit sphere.
 */
class Sphere(
    val transformationMatrix: Matrix = Matrix.identity(4),
    val material: Material = Material(
        surfaceColor = Tuple.color(1.0, 1.0, 1.0),
        ambientReflection = 0.1,
        diffuseReflection = 0.9,
        specularReflection = 0.9,
        shininess = 200.0
    )
) {

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

    /**
     * Calculates the shade which makes the object appear three-dimensional.
     */
    fun lightning(pointLight: PointLight, illuminatedPoint: Tuple, eyeVector: Tuple, normalVector: Tuple): Tuple {
        val effectiveColor = material.surfaceColor.hadamardProduct(pointLight.intensity)
        val lightVector = (pointLight.position - illuminatedPoint).normalize()
        val lightDotNormal = lightVector.dot(normalVector)

        val ambientContribution = effectiveColor * material.ambientReflection

        val diffuseContribution =
            if (lightDotNormal < 0) Tuple.black
            else effectiveColor * material.diffuseReflection * lightDotNormal

        val specularContribution = if (lightDotNormal < 0) Tuple.black else {
            val reflectVector = (-lightVector).reflectAround(normalVector)
            val reflectDotEye = reflectVector.dot(eyeVector)
            if (reflectDotEye <= 0) Tuple.black
            else pointLight.intensity * material.specularReflection * reflectDotEye.pow(material.shininess)
        }

        return ambientContribution + diffuseContribution + specularContribution
    }

}
