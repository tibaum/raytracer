package raytracer.elements

import raytracer.elements.Tuple.Companion.black
import raytracer.elements.Tuple.Companion.white
import kotlin.math.floor

class GradientPattern(
    private val color1: Tuple = white,
    private val color2: Tuple = black,
    transformationMatrix: Matrix = Matrix.identity(4)
) : Pattern(transformationMatrix) {

    override fun patternAt(point: Tuple): Tuple {
        val distance = color2 - color1
        val fraction = point[0] - floor(point[0])
        return color1 + distance * fraction
    }

}
