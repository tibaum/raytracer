package raytracer.elements

abstract class Pattern(val transformationMatrix: Matrix) {

    private val inverseTransformationMatrix = transformationMatrix.inverse()

    fun patternAtShape(shape: Shape, worldPoint: Tuple): Tuple {
        val objectPoint = shape.worldToObject(worldPoint)
        val patternPoint = inverseTransformationMatrix * objectPoint
        return patternAt(patternPoint)
    }

    abstract fun patternAt(point: Tuple): Tuple

}
