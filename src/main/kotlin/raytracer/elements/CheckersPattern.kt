package raytracer.elements

import raytracer.elements.Tuple.Companion.black
import raytracer.elements.Tuple.Companion.white
import kotlin.math.floor

class CheckersPattern(
    private val color1: Tuple = white,
    private val color2: Tuple = black,
    transformationMatrix: Matrix = Matrix.identity(4)
) : Pattern(transformationMatrix) {

    override fun patternAt(point: Tuple): Tuple =
        if ((floor(point[0] + floor(point[1]) + floor(point[2])).mod(2.0)) == 0.0) color1
        else color2

}
