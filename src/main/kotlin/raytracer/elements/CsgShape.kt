package raytracer.elements

import raytracer.elements.CsgOperation.*

/**
 * CSG = Constructive Solid Geometry
 */
class CsgShape(
    val left: Shape,
    val right: Shape,
    val operation: CsgOperation
) : Shape(Matrix.identity(4), Material()) {

    init {
        left.parent = this
        right.parent = this
    }

    override fun includes(shape: Shape): Boolean =
        (shape is CsgShape && this == shape) || left.includes(shape) || right.includes(shape)

    override fun localIntersect(ray: Ray): Intersections {
        val intersections = left.intersect(ray).accumulate(right.intersect(ray))
        return filterIntersections(intersections)
    }

    fun filterIntersections(intersections: Intersections): Intersections {
        val result = mutableListOf<Intersection>()
        var hitInsideLeft = false
        var hitInsideRight = false
        for (intersection in intersections) {
            val leftHit = left.includes(intersection.shape)
            if (intersectionAllowed(operation, leftHit, hitInsideLeft, hitInsideRight))
                result.add(intersection)
            if (leftHit) hitInsideLeft = !hitInsideLeft
            else hitInsideRight = !hitInsideRight
        }
        return Intersections(*result.toTypedArray())
    }

    override fun localNormalAt(point: Tuple, hit: Intersection?): Tuple {
        throw UnsupportedOperationException("call localNormalAt on the concrete shapes in this CSG shape")
    }

    override fun localBoundingBox(): BoundingBox =
        left.boundingBox().accumulate(right.boundingBox())

    companion object {
        fun intersectionAllowed(
            operation: CsgOperation,
            leftHit: Boolean,
            hitInsideLeft: Boolean,
            hitInsideRight: Boolean
        ): Boolean = when (operation) {
            UNION -> (leftHit && !hitInsideRight) || (!leftHit && !hitInsideLeft)
            INTERSECTION -> (leftHit && hitInsideRight) || (!leftHit && hitInsideLeft)
            DIFFERENCE -> (leftHit && !hitInsideRight) || (!leftHit && hitInsideLeft)
        }
    }

}
