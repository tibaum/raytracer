package raytracer.elements

import kotlin.math.abs

fun almostEqual(x: Double, y: Double) = abs(x - y) < 0.00001
