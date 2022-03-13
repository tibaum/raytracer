package raytracer.elements

import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Encapsulates parameters of an intersection occurrence.
 */
data class ShadingPreComputation(
    val hitTime: Double,
    val shape: Shape,
    val intersectionPoint: Tuple,
    val overPoint: Tuple,
    val eyeVector: Tuple,
    val normalVector: Tuple,
    val intersectionOccursInside: Boolean,
    val reflectVector: Tuple,
    val refractiveIndexOfMaterialExited: Double,
    val refractiveIndexOfMaterialEntered: Double,
    val underPoint: Tuple
) {

    init {
        require(intersectionPoint.isPoint())
        require(overPoint.isPoint())
        require(eyeVector.isVector())
        require(normalVector.isVector())
        require(reflectVector.isVector())
        require(refractiveIndexOfMaterialExited >= 0.0)
        require(refractiveIndexOfMaterialEntered >= 0.0)
        require(underPoint.isPoint())
    }

    fun schlick(): Double {
        var cos = eyeVector.dot(normalVector)
        if (refractiveIndexOfMaterialExited > refractiveIndexOfMaterialEntered) {
            val n = refractiveIndexOfMaterialExited / refractiveIndexOfMaterialEntered
            val sin2T = n.pow(2) * (1 - cos.pow(2))
            if (sin2T > 1)
                return 1.0
            val cosT = sqrt(1 - sin2T)
            cos = cosT
        }
        val r0 = ((refractiveIndexOfMaterialExited - refractiveIndexOfMaterialEntered)
                / (refractiveIndexOfMaterialExited + refractiveIndexOfMaterialEntered))
            .pow(2)
        return r0 + (1 - r0) * (1 - cos).pow(5)
    }

    companion object {
        fun of(
            ray: Ray,
            intersection: Intersection,
            intersections: Intersections = Intersections(intersection)
        ): ShadingPreComputation {
            val pointWhereRayHitsSphere = ray.position(intersection.time)
            val eyeVector = -ray.direction
            val normalVector = intersection.shape.normalAt(pointWhereRayHitsSphere)
            val intersectionOccursInside = normalVector.dot(eyeVector) < 0
            val resultingNormalVector = if (intersectionOccursInside) -normalVector else normalVector
            val reflectVector = ray.direction.reflectAround(resultingNormalVector)
            val overPoint = pointWhereRayHitsSphere + resultingNormalVector * EPSILON
            val underPoint = pointWhereRayHitsSphere - resultingNormalVector * EPSILON
            val (refractiveIndexOfMaterialExited, refractiveIndexOfMaterialEntered) =
                computeRefractiveIndices(intersection, intersections)
            return ShadingPreComputation(
                intersection.time,
                intersection.shape,
                pointWhereRayHitsSphere,
                overPoint,
                eyeVector,
                resultingNormalVector,
                intersectionOccursInside,
                reflectVector,
                refractiveIndexOfMaterialExited,
                refractiveIndexOfMaterialEntered,
                underPoint
            )
        }

        private fun computeRefractiveIndices(hit: Intersection, intersections: Intersections): Pair<Double, Double> {
            var refractiveIndexOfMaterialExited = 0.0
            var refractiveIndexOfMaterialEntered = 0.0
            val containers = mutableListOf<Shape>()

            for (intersection in intersections) {
                if (intersection == hit) refractiveIndexOfMaterialExited =
                    if (containers.isEmpty()) 1.0
                    else containers.last().material.refractiveIndex

                if (intersection.shape in containers) containers.remove(intersection.shape)
                else containers.add(intersection.shape)

                if (intersection == hit) {
                    refractiveIndexOfMaterialEntered =
                        if (containers.isEmpty()) 1.0
                        else containers.last().material.refractiveIndex
                    break
                }
            }

            return Pair(refractiveIndexOfMaterialExited, refractiveIndexOfMaterialEntered)
        }
    }

}
