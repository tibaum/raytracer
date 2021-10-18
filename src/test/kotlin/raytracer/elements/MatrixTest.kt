package raytracer.elements

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MatrixTest {

    @Test
    fun testConstruction2x2() {
        val matrix = Matrix(
            Dim(2, 2),
            -3.0, 5.0,
            1.0, -2.0
        )
        assertEquals(-3.0, matrix[0, 0])
        assertEquals(5.0, matrix[0, 1])
        assertEquals(1.0, matrix[1, 0])
        assertEquals(-2.0, matrix[1, 1])
    }

    @Test
    fun testConstruction3x3() {
        val matrix = Matrix(
            Dim(3, 3),
            -3.0, 5.0, 0.0,
            1.0, -2.0, -7.0,
            0.0, 1.0, 1.0
        )
        assertEquals(-3.0, matrix[0, 0])
        assertEquals(-2.0, matrix[1, 1])
        assertEquals(1.0, matrix[2, 2])
    }

    @Test
    fun testConstruction4x4() {
        val matrix = Matrix(
            Dim(4, 4),
            1.0, 2.0, 3.0, 4.0,
            5.5, 6.5, 7.5, 8.5,
            9.0, 10.0, 11.0, 12.0,
            13.5, 14.5, 15.5, 16.5
        )
        assertEquals(1.0, matrix[0, 0])
        assertEquals(4.0, matrix[0, 3])
        assertEquals(5.5, matrix[1, 0])
        assertEquals(7.5, matrix[1, 2])
        assertEquals(11.0, matrix[2, 2])
        assertEquals(13.5, matrix[3, 0])
        assertEquals(15.5, matrix[3, 2])
    }

    @Test
    fun testDimIsValidByConstruction() {
        assertThrows(IllegalArgumentException::class.java) { Dim(0, 1) }
        assertThrows(IllegalArgumentException::class.java) { Dim(1, 0) }
        assertThrows(IllegalArgumentException::class.java) { Dim(-1, 1) }
        assertThrows(IllegalArgumentException::class.java) { Dim(1, -1) }
    }

    @Test
    fun testConstructionFailsBecauseDimIsNotSatisfied() {
        assertThrows(IllegalArgumentException::class.java) { Matrix(Dim(2, 2), -3.0, 5.0, 1.0) }
    }

    @Test
    fun testAccessOfEntryOutOfBounds() {
        val matrix = Matrix(
            Dim(2, 2),
            -3.0, 5.0,
            1.0, -2.0
        )
        assertThrows(IndexOutOfBoundsException::class.java) { matrix[0, 2] }
    }

    @Test
    fun testEquality4x4() {
        val matrixA = Matrix(
            Dim(4, 4),
            1.0, 2.0, 3.0, 4.0,
            5.0, 6.0, 7.0, 8.0,
            9.0, 8.0, 7.0, 6.0,
            5.0, 4.0, 3.0, 2.0
        )
        val matrixB = Matrix(
            Dim(4, 4),
            1.0, 2.0, 3.0, 4.0,
            5.0, 6.0, 7.0, 8.0,
            9.0, 8.0, 7.0, 6.0,
            5.0, 4.0, 3.0, 2.0
        )
        assertTrue(matrixA == matrixB)
    }

    @Test
    fun testEqualityDifferentDim() {
        val matrixA = Matrix(Dim(1, 1), 1.0)
        val matrixB = Matrix(Dim(1, 2), 1.0, 2.0)
        assertFalse(matrixA == matrixB)
    }

    @Test
    fun testUnequality4x4() {
        val matrixA = Matrix(
            Dim(4, 4),
            1.0, 2.0, 3.0, 4.0,
            5.0, 6.0, 7.0, 8.0,
            9.0, 8.0, 7.0, 6.0,
            5.0, 4.0, 3.0, 2.0
        )
        val matrixB = Matrix(
            Dim(4, 4),
            2.0, 3.0, 4.0, 5.0,
            6.0, 7.0, 8.0, 9.0,
            8.0, 7.0, 6.0, 5.0,
            4.0, 3.0, 2.0, 1.0
        )
        assertTrue(matrixA != matrixB)
    }

    @Test
    fun testAlmostEqualIsEnough() {
        val matrixA = Matrix(
            Dim(2, 2),
            1.0, 2.000001,
            5.0, 5.999999
        )
        val matrixB = Matrix(
            Dim(2, 2),
            1.0, 2.0,
            5.0, 6.0
        )
        assertTrue(matrixA == matrixB)
    }

    @Test
    fun testMultiplyTwoMatrices() {
        val matrixA = Matrix(
            Dim(4, 4),
            1.0, 2.0, 3.0, 4.0,
            5.0, 6.0, 7.0, 8.0,
            9.0, 8.0, 7.0, 6.0,
            5.0, 4.0, 3.0, 2.0
        )
        val matrixB = Matrix(
            Dim(4, 4),
            -2.0, 1.0, 2.0, 3.0,
            3.0, 2.0, 1.0, -1.0,
            4.0, 3.0, 6.0, 5.0,
            1.0, 2.0, 7.0, 8.0
        )
        val expectedProduct = Matrix(
            Dim(4, 4),
            20.0, 22.0, 50.0, 48.0,
            44.0, 54.0, 114.0, 108.0,
            40.0, 58.0, 110.0, 102.0,
            16.0, 26.0, 46.0, 42.0
        )
        assertEquals(expectedProduct, matrixA * matrixB)
    }

    @Test
    fun testMultiplyTwoMatricesWithDifferentDim() {
        val matrixA = Matrix(
            Dim(2, 2),
            1.0, 2.0,
            3.0, 4.0
        )
        val matrixB = Matrix(
            Dim(2, 1),
            -2.0,
            3.0
        )
        val expectedProduct = Matrix(
            Dim(2, 1),
            4.0,
            6.0
        )
        assertEquals(expectedProduct, matrixA * matrixB)
    }

    @Test
    fun testMultiplyNotPossible() {
        val matrixA = Matrix(
            Dim(2, 1),
            -2.0,
            3.0
        )
        val matrixB = Matrix(
            Dim(2, 2),
            1.0, 2.0,
            3.0, 4.0
        )
        assertThrows(java.lang.IllegalArgumentException::class.java) { matrixA * matrixB }
    }

    @Test
    fun testMultiplyColumnByRow() {
        val matrixA = Matrix(
            Dim(2, 1),
            -2.0,
            3.0
        )
        val matrixB = Matrix(
            Dim(1, 2),
            4.0, 2.0
        )
        val expectedProduct = Matrix(
            Dim(2, 2),
            -8.0, -4.0,
            12.0, 6.0
        )
        assertEquals(expectedProduct, matrixA * matrixB)
    }

    @Test
    fun testMultiplyMatrixWithTuple() {
        val matrix = Matrix(
            Dim(4, 4),
            1.0, 2.0, 3.0, 4.0,
            2.0, 4.0, 4.0, 2.0,
            8.0, 6.0, 4.0, 1.0,
            0.0, 0.0, 0.0, 1.0
        )
        val tuple = Tuple(1.0, 2.0, 3.0, 1.0)
        assertEquals(Tuple(18.0, 24.0, 33.0, 1.0), matrix * tuple)
    }

    @Test
    fun testMultyplyByIdentityMatrix() {
        val matrixA = Matrix(
            Dim(4, 4),
            0.0, 1.0, 2.0, 4.0,
            1.0, 2.0, 4.0, 8.0,
            2.0, 4.0, 8.0, 16.0,
            4.0, 8.0, 16.0, 32.0
        )
        assertEquals(matrixA, matrixA * Matrix.identity(4))
    }

    @Test
    fun testTranspose() {
        val matrix = Matrix(
            Dim(4, 4),
            0.0, 9.0, 3.0, 0.0,
            9.0, 8.0, 0.0, 8.0,
            1.0, 8.0, 5.0, 3.0,
            0.0, 0.0, 5.0, 8.0
        )
        val transposed = Matrix(
            Dim(4, 4),
            0.0, 9.0, 1.0, 0.0,
            9.0, 8.0, 8.0, 0.0,
            3.0, 0.0, 5.0, 5.0,
            0.0, 8.0, 3.0, 8.0
        )
        assertEquals(transposed, matrix.transpose())
    }

    @Test
    fun testTransposeIdentity() {
        assertEquals(Matrix.identity(4), Matrix.identity(4).transpose())
    }

    @Test
    fun testDeterminantOf2x2Matrix() {
        val matrix = Matrix(
            Dim(2, 2),
            1.0, 5.0,
            -3.0, 2.0
        )
        assertEquals(17.0, matrix.det())
    }

    @Test
    fun testSubmatrixOf3x3() {
        val matrix = Matrix(
            Dim(3, 3),
            1.0, 5.0, 0.0,
            -3.0, 2.0, 7.0,
            0.0, 6.0, -3.0
        )
        val expectedSubmatrix = Matrix(
            Dim(2, 2),
            -3.0, 2.0,
            0.0, 6.0
        )
        assertEquals(expectedSubmatrix, matrix.submatrix(0, 2))
    }

    @Test
    fun testSubmatrixOf4x4() {
        val matrix = Matrix(
            Dim(4, 4),
            -6.0, 1.0, 1.0, 6.0,
            -8.0, 5.0, 8.0, 6.0,
            -1.0, 0.0, 8.0, 2.0,
            -7.0, 1.0, -1.0, 1.0
        )
        val expectedSubmatrix = Matrix(
            Dim(3, 3),
            -6.0, 1.0, 6.0,
            -8.0, 8.0, 6.0,
            -7.0, -1.0, 1.0
        )
        assertEquals(expectedSubmatrix, matrix.submatrix(2, 1))
    }

}
