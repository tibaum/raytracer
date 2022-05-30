package raytracer.elements

import raytracer.elements.Tuple.Companion.origin
import raytracer.elements.Tuple.Companion.point
import kotlin.Double.Companion.NEGATIVE_INFINITY
import kotlin.Double.Companion.POSITIVE_INFINITY
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

abstract class Shape(
    val transformationMatrix: Matrix,
    val material: Material
) {

    var group: Group? = null
    var parent: CsgShape? = null

    private val inverseTransformation: Matrix = transformationMatrix.inverse()
    private val transposedInverseTransformation: Matrix = inverseTransformation.transpose()

    val center = origin

    open fun includes(shape: Shape): Boolean = this == shape

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
    fun normalAt(point: Tuple, hit: Intersection? = null): Tuple {
        val localPoint = worldToObject(point)
        val localNormal = localNormalAt(localPoint, hit)
        return normalToWorld(localNormal)
    }

    abstract fun localNormalAt(point: Tuple, hit: Intersection? = null): Tuple

    /**
     * Converts a point from world space to object space.
     */
    fun worldToObject(point: Tuple): Tuple {
        val p = group?.worldToObject(point) ?: point
        return inverseTransformation * p
    }

    /**
     * Converts a normal vector from object space to world space.
     */
    fun normalToWorld(normal: Tuple): Tuple {
        val n = (transposedInverseTransformation * normal).asVector().normalize()
        return group?.normalToWorld(n) ?: n
    }

    fun boundingBox(): BoundingBox {
        val box = localBoundingBox()
        val (xMin, yMin, zMin) = box.min
        val (xMax, yMax, zMax) = box.max
        val corners = listOf(
            point(xMin, yMin, zMin),
            point(xMin, yMin, zMax),
            point(xMin, yMax, zMin),
            point(xMin, yMax, zMax),
            point(xMax, yMin, zMin),
            point(xMax, yMin, zMax),
            point(xMax, yMax, zMin),
            point(xMax, yMax, zMax)
        )
        val cornersInGroupSpace = corners.map { transformationMatrix * it }
        val min = cornersInGroupSpace.reduce { first, second -> first.elementWise(second, ::min) }
        val max = cornersInGroupSpace.reduce { first, second -> first.elementWise(second, ::max) }
        return BoundingBox(replaceNaN(min, NEGATIVE_INFINITY), replaceNaN(max, POSITIVE_INFINITY))
    }

    abstract fun localBoundingBox(): BoundingBox

    private fun replaceNaN(point: Tuple, replacement: Double): Tuple {
        val backingArray = point.toDoubleArray()
            .map { if (it.isNaN()) replacement else it }
            .toDoubleArray()
        backingArray[3] = 1.0
        return Tuple(*backingArray)
    }

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
        val color =
            if (material.pattern == null) material.surfaceColor
            else material.pattern.patternAtShape(this, illuminatedPoint)
        val effectiveColor = color.hadamardProduct(pointLight.intensity)
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
