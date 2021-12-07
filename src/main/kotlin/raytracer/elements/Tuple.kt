package raytracer.elements

import kotlin.math.sqrt

class Tuple(private vararg val values: Double) {

    companion object {
        fun vector(x: Double, y: Double, z: Double) = Tuple(x, y, z, 0.0)
        fun point(x: Double, y: Double, z: Double) = Tuple(x, y, z, 1.0)
    }

    val size: Int
        get() = values.size

    fun toDoubleArray(): DoubleArray = values.copyOf()

    fun isPoint() = values.size == 4 && values[3] == 1.0
    fun isVector() = values.size == 4 && values[3] == 0.0

    /**
     * Creates a tuple with the first three elements unchanged and the fourth element set to 0.0.
     * @throws IllegalArgumentException if [size] is not equal to 4
     */
    fun asVector(): Tuple {
        assertSize4()
        val backingArray = toDoubleArray()
        backingArray[3] = 0.0
        return Tuple(*backingArray)
    }

    private fun assertSize4() {
        if (size != 4)
            throw IllegalArgumentException("size must be equal to 4")
    }

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is Tuple) return false
        if (other.size != this.size) return false
        for (i in values.indices)
            if (!almostEqual(values[i], other.values[i]))
                return false
        return true
    }

    override fun hashCode(): Int {
        return values.contentHashCode()
    }

    operator fun get(index: Int): Double {
        if (index < 0 || index > values.size - 1)
            throw IndexOutOfBoundsException("index=$index but tuple-size=${values.size}")
        return values[index]
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
typealias Point = Tuple
typealias Vector = Tuple
