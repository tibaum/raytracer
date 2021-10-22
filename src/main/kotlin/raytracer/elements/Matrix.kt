package raytracer.elements

data class Dim(val nrow: Int, val ncol: Int) {
    init {
        if (nrow < 1 || ncol < 1)
            throw IllegalArgumentException("nrow and ncol must be greater than 0")
    }
}

class Matrix(private val dim: Dim, private vararg val entries: Double) {

    init {
        if (entries.size != dim.nrow * dim.ncol)
            throw IllegalArgumentException("entries.size=${entries.size}, dim.nrow=${dim.nrow}, dim.ncol=${dim.ncol}")
    }

    companion object {
        fun identity(size: Int): Matrix {
            val entries = DoubleArray(size * size)
            for (k in 0 until size)
                entries[k * (size + 1)] = 1.0
            return Matrix(Dim(size, size), *entries)
        }
    }

    operator fun get(row: Int, column: Int): Double {
        if (row < 0 || row >= dim.nrow || column < 0 || column >= dim.ncol)
            throw IndexOutOfBoundsException("row=$row, column=$column but dim=$dim")
        return entries[index(row, column)]
    }

    override fun toString(): String {
        var result = ""
        for (i in 0 until dim.nrow) {
            for (j in 0 until dim.ncol) {
                result += "${this[i, j]} "
            }
            result += "\n"
        }
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Matrix

        if (dim != other.dim) return false

        for (i in entries.indices) {
            if (!almostEqual(entries[i], other.entries[i])) return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = dim.hashCode()
        result = 31 * result + entries.contentHashCode()
        return result
    }

    operator fun times(other: Matrix): Matrix {
        if (dim.ncol != other.dim.nrow)
            throw IllegalArgumentException()

        fun entry(row: Int, column: Int): Double {
            var result = 0.0
            for (k in 0 until dim.ncol)
                result += this[row, k] * other[k, column]
            return result
        }

        val entries = DoubleArray(dim.nrow * other.dim.ncol)
        for (row in 0 until dim.nrow)
            for (column in 0 until other.dim.ncol)
                entries[row * other.dim.ncol + column] = entry(row, column)

        return Matrix(Dim(dim.nrow, other.dim.ncol), *entries)
    }

    operator fun times(tuple: Tuple): Tuple {
        val other = Matrix(Dim(tuple.size, 1), *tuple.toDoubleArray())
        val matrix = this * other
        return Tuple(*matrix.entries)
    }

    fun transpose(): Matrix {
        val entries = DoubleArray(dim.nrow * dim.ncol)
        for (row in 0 until dim.nrow)
            for (column in 0 until dim.ncol)
                entries[index(row, column)] = this[column, row]
        return Matrix(Dim(dim.nrow, dim.ncol), *entries)
    }

    fun det(): Double {
        return this[0, 0] * this[1, 1] - this[0, 1] * this[1, 0]
    }

    fun submatrix(row: Int, column: Int): Matrix {
        val entries = DoubleArray((dim.nrow - 1) * (dim.ncol - 1))
        for (oldRow in 0 until dim.nrow) {
            for (oldColumn in 0 until dim.ncol) {
                if (oldRow == row || oldColumn == column) continue
                val newRow = if (oldRow > row) oldRow - 1 else oldRow
                val newColumn = if (oldColumn > column) oldColumn - 1 else oldColumn
                entries[newRow * (dim.ncol - 1) + newColumn] = this[oldRow, oldColumn]
            }
        }
        return Matrix(Dim(dim.nrow - 1, dim.ncol - 1), *entries)
    }

    private fun index(row: Int, column: Int) = row * dim.ncol + column

}
