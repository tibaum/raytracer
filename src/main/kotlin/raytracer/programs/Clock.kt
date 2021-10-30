package raytracer.programs

import raytracer.elements.Canvas
import raytracer.elements.Color
import raytracer.elements.Matrix
import raytracer.elements.Tuple
import java.io.File
import kotlin.math.PI
import kotlin.math.roundToInt

/**
 * Computes the positions of the hours of a clock face and draws them on a canvas.
 */
fun main() {

    // orient the clock around the y-axis
    val oneHourRotation = Matrix.rotationY(radians = PI / 6)

    // starting point from which the positions of the other hours are calculated
    val twelveOClock = Tuple.point(0.0, 0.0, 1.0)

    val hours = mutableListOf(twelveOClock)
    while (hours.size < 12) {
        val nextHour = oneHourRotation * hours.last()
        hours.add(nextHour)
    }

    val size = 500
    val canvas = Canvas(size, size)

    // center of the clock lies in the middle of the canvas
    val center = Tuple.point(size / 2.0, 0.0, size / 2.0)

    val clockRadius = size * 3 / 8.0

    // scaling of the x- and z-coordinates of a point
    val clockRadiusScaling = Matrix.scaling(clockRadius, 0.0, clockRadius)

    hours.map { hour -> clockRadiusScaling * hour + center }
        .forEach { hour -> canvas.writePixel(hour[0].roundToInt(), hour[2].roundToInt(), Color(1.0, 1.0, 1.0)) }

    File("clock.ppm").writeText(canvas.toPPM())

}