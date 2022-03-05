package raytracer.elements

/**
 * Encapsulates parameters of an intersection occurrence.
 */
data class ShadingPreComputation(
    val hitTime: Double,
    val sphere: Sphere,
    val intersectionPoint: Tuple,
    val overPoint: Tuple,
    val eyeVector: Tuple,
    val normalVector: Tuple,
    val intersectionOccursInside: Boolean
) {

    init {
        require(intersectionPoint.isPoint())
        require(overPoint.isPoint())
        require(eyeVector.isVector())
        require(normalVector.isVector())
    }

    companion object {
        fun of(ray: Ray, intersection: Intersection): ShadingPreComputation {
            val pointWhereRayHitsSphere = ray.position(intersection.time)
            val eyeVector = -ray.direction
            val normalVector = intersection.sphere.normalAt(pointWhereRayHitsSphere)
            val intersectionOccursInside = normalVector.dot(eyeVector) < 0
            val resultingNormalVector = if (intersectionOccursInside) -normalVector else normalVector
            val overPoint = pointWhereRayHitsSphere + resultingNormalVector * EPSILON
            return ShadingPreComputation(
                intersection.time,
                intersection.sphere,
                pointWhereRayHitsSphere,
                overPoint,
                eyeVector,
                resultingNormalVector,
                intersectionOccursInside
            )
        }
    }

}