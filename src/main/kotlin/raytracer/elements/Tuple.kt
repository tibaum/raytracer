package raytracer.elements

import kotlin.math.sqrt

class Tuple(vararg val values: Double) {

    companion object {
        fun createVector(x: Double, y: Double, z: Double) = Tuple(x, y, z, 0.0)
        fun createPoint(x: Double, y: Double, z: Double) = Tuple(x, y, z, 1.0)
    }

    fun isPoint() = values.size == 4 && values[3] == 1.0
    fun isVector() = values.size == 4 && values[3] == 0.0

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is Tuple) return false
        for (i in values.indices)
            if (!almostEqual(values[i], other.values[i]))
                return false
        return true
    }

    operator fun plus(other: Tuple): Tuple {
        val newValues = DoubleArray(values.size)
        for (i in values.indices)
            newValues[i] = values[i] + other.values[i]
        return Tuple(*newValues)
    }

    operator fun minus(other: Tuple): Tuple {
        val newValues = DoubleArray(values.size)
        for (i in values.indices)
            newValues[i] = values[i] - other.values[i]
        return Tuple(*newValues)
    }

    operator fun unaryMinus(): Tuple {
        val newValues = DoubleArray(values.size)
        for (i in values.indices)
            newValues[i] = -values[i]
        return Tuple(*newValues)
    }

    override fun toString(): String {
        return "Tuple[${values.joinToString()}]"
    }

    operator fun times(d: Double): Tuple {
        val newValues = DoubleArray(values.size)
        for (i in values.indices)
            newValues[i] = values[i] * d
        return Tuple(*newValues)
    }

    operator fun div(d: Double): Tuple {
        val newValues = DoubleArray(values.size)
        for (i in values.indices)
            newValues[i] = values[i] / d
        return Tuple(*newValues)
    }

    // Works especially for vectors because the fourth value is defined to be 0.0 and adds nothing to the calculation
    fun magnitude(): Double = sqrt(values.sumOf { value -> value * value })

    fun normalize() = this / magnitude()

    fun dot(other: Tuple): Double {
        var dotProduct = 0.0
        for (i in values.indices)
            dotProduct += values[i] * other.values[i]
        return dotProduct
    }

    /**
     * Specifically for 3-dim vectors! Ignores all values from the 4th dimension on.
     */
    fun cross(other: Tuple): Tuple {
        return Tuple(
            values[1] * other.values[2] - values[2] * other.values[1],
            values[2] * other.values[0] - values[0] * other.values[2],
            values[0] * other.values[1] - values[1] * other.values[0]
        )
    }

    fun hadamardProduct(other: Tuple): Tuple {
        val newValues = DoubleArray(values.size)
        for (i in values.indices)
            newValues[i] = values[i] * other.values[i]
        return Tuple(*newValues)
    }

}

typealias Color = Tuple
