package raytracer.elements

import kotlin.math.abs

const val EPSILON = 0.00001

fun almostEqual(x: Double, y: Double) = abs(x - y) < EPSILON
