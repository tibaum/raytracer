package raytracer.elements

import kotlin.math.floor

class StripePattern(
    private val color1: Tuple = Tuple.white,
    private val color2: Tuple = Tuple.black,
    transformationMatrix: Matrix = Matrix.identity(4)
) : Pattern(transformationMatrix) {

    override fun patternAt(point: Tuple): Tuple =
        if (floor(point[0]).mod(2.0) == 0.0) color1
        else color2

}
