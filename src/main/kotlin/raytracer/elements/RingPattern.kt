package raytracer.elements

import raytracer.elements.Tuple.Companion.black
import raytracer.elements.Tuple.Companion.white
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

class RingPattern(
    private val color1: Tuple = white,
    private val color2: Tuple = black,
    transformationMatrix: Matrix = Matrix.identity(4)
) : Pattern(transformationMatrix) {

    override fun patternAt(point: Tuple): Tuple =
        if (floor(sqrt(point[0].pow(2) + point[2].pow(2))).mod(2.0) == 0.0) color1
        else color2

}
