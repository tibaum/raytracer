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
    fun testDeterminantOf1x1Matrix() {
        val matrix = Matrix(Dim(1, 1), 5.0)
        assertEquals(5.0, matrix.det())
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
    fun testSubmatrixOf2x2At00() {
        val matrix = Matrix(
            Dim(2, 2),
            1.0, 5.0,
            -3.0, 2.0
        )
        val expectedSubmatrix = Matrix(
            Dim(1, 1),
            2.0
        )
        assertEquals(expectedSubmatrix, matrix.submatrix(0, 0))
    }

    @Test
    fun testSubmatrixOf2x2At01() {
        val matrix = Matrix(
            Dim(2, 2),
            1.0, 5.0,
            -3.0, 2.0
        )
        val expectedSubmatrix = Matrix(
            Dim(1, 1),
            -3.0
        )
        assertEquals(expectedSubmatrix, matrix.submatrix(0, 1))
    }

    @Test
    fun testSubmatrixOf2x2At10() {
        val matrix = Matrix(
            Dim(2, 2),
            1.0, 5.0,
            -3.0, 2.0
        )
        val expectedSubmatrix = Matrix(
            Dim(1, 1),
            5.0
        )
        assertEquals(expectedSubmatrix, matrix.submatrix(1, 0))
    }

    @Test
    fun testSubmatrixOf2x2At11() {
        val matrix = Matrix(
            Dim(2, 2),
            1.0, 5.0,
            -3.0, 2.0
        )
        val expectedSubmatrix = Matrix(
            Dim(1, 1),
            1.0
        )
        assertEquals(expectedSubmatrix, matrix.submatrix(1, 1))
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

    @Test
    fun testMinor() {
        val matrixA = Matrix(
            Dim(3, 3),
            3.0, 5.0, 0.0,
            2.0, -1.0, -7.0,
            6.0, -1.0, 5.0
        )
        val matrixB = matrixA.submatrix(1, 0)
        assertEquals(25.0, matrixB.det())
        assertEquals(25.0, matrixA.minor(1, 0))
    }

    @Test
    fun testCofactor() {
        val matrix = Matrix(
            Dim(3, 3),
            3.0, 5.0, 0.0,
            2.0, -1.0, -7.0,
            6.0, -1.0, 5.0
        )
        assertEquals(-12.0, matrix.minor(0, 0))
        assertEquals(-12.0, matrix.cofactor(0, 0))
        assertEquals(25.0, matrix.minor(1, 0))
        assertEquals(-25.0, matrix.cofactor(1, 0))
    }

    @Test
    fun testDeterminatOf3x3Matrix() {
        val matrix = Matrix(
            Dim(3, 3),
            1.0, 2.0, 6.0,
            -5.0, 8.0, -4.0,
            2.0, 6.0, 4.0
        )
        assertEquals(56.0, matrix.cofactor(0, 0))
        assertEquals(12.0, matrix.cofactor(0, 1))
        assertEquals(-46.0, matrix.cofactor(0, 2))
        assertEquals(-196.0, matrix.det())
    }

    @Test
    fun testDeterminantOf4x4Matrix() {
        val matrix = Matrix(
            Dim(4, 4),
            -2.0, -8.0, 3.0, 5.0,
            -3.0, 1.0, 7.0, 3.0,
            1.0, 2.0, -9.0, 6.0,
            -6.0, 7.0, 7.0, -9.0
        )
        assertEquals(690.0, matrix.cofactor(0, 0))
        assertEquals(447.0, matrix.cofactor(0, 1))
        assertEquals(210.0, matrix.cofactor(0, 2))
        assertEquals(51.0, matrix.cofactor(0, 3))
        assertEquals(-4071.0, matrix.det())
    }

    @Test
    fun testIsInvertible() {
        val matrix = Matrix(
            Dim(4, 4),
            6.0, 4.0, 4.0, 4.0,
            5.0, 5.0, 7.0, 6.0,
            4.0, -9.0, 3.0, -7.0,
            9.0, 1.0, 7.0, -6.0
        )
        assertEquals(-2120.0, matrix.det())
        assertTrue(matrix.isInvertible())
    }

    @Test
    fun testIsNotInvertible() {
        val matrix = Matrix(
            Dim(4, 4),
            -4.0, 2.0, -2.0, -3.0,
            9.0, 6.0, 2.0, 6.0,
            0.0, -5.0, 1.0, -5.0,
            0.0, 0.0, 0.0, 0.0
        )
        assertEquals(0.0, matrix.det())
        assertFalse(matrix.isInvertible())
    }


    @Test
    fun testIsInvertible1x1() {
        val matrix = Matrix(Dim(1, 1), 0.00001)
        assertEquals(0.00001, matrix.det())
        assertTrue(matrix.isInvertible())
    }

    @Test
    fun testIsNotInvertible1x1() {
        val matrix = Matrix(Dim(1, 1), 0.0)
        assertEquals(0.0, matrix.det())
        assertFalse(matrix.isInvertible())
    }

    @Test
    fun testInverse() {
        val matrix = Matrix(
            Dim(4, 4),
            -5.0, 2.0, 6.0, -8.0,
            1.0, -5.0, 1.0, 8.0,
            7.0, 7.0, -6.0, -7.0,
            1.0, -3.0, 7.0, 4.0
        )
        val inverseMatrix = matrix.inverse()
        assertEquals(532.0, matrix.det())
        assertEquals(-160.0, matrix.cofactor(2, 3))
        assertEquals(-160.0 / 532.0, inverseMatrix[3, 2])
        assertEquals(105.0, matrix.cofactor(3, 2))
        assertEquals(105.0 / 532.0, inverseMatrix[2, 3])

        val expectedInverse = Matrix(
            Dim(4, 4),
            0.21805, 0.45113, 0.24060, -0.04511,
            -0.80827, -1.45677, -0.44361, 0.52068,
            -0.07895, -0.22368, -0.05263, 0.19737,
            -0.52256, -0.81391, -0.30075, 0.30639
        )
        assertEquals(expectedInverse, inverseMatrix)
    }

    @Test
    fun testInverse2() {
        val matrix = Matrix(
            Dim(4, 4),
            8.0, -5.0, 9.0, 2.0,
            7.0, 5.0, 6.0, 1.0,
            -6.0, 0.0, 9.0, 6.0,
            -3.0, 0.0, -9.0, -4.0
        )
        val expectedInverse = Matrix(
            Dim(4, 4),
            -0.15385, -0.15385, -0.28205, -0.53846,
            -0.07692, 0.12308, 0.02564, 0.03077,
            0.35897, 0.35897, 0.43590, 0.92308,
            -0.69231, -0.69231, -0.76923, -1.92308
        )
        assertEquals(expectedInverse, matrix.inverse())
    }

    @Test
    fun testInverse3() {
        val matrix = Matrix(
            Dim(4, 4),
            9.0, 3.0, 0.0, 9.0,
            -5.0, -2.0, -6.0, -3.0,
            -4.0, 9.0, 6.0, 4.0,
            -7.0, 6.0, 6.0, 2.0
        )
        val expectedInverse = Matrix(
            Dim(4, 4),
            -0.04074, -0.07778, 0.14444, -0.22222,
            -0.07778, 0.03333, 0.36667, -0.33333,
            -0.02901, -0.14630, -0.10926, 0.12963,
            0.17778, 0.06667, -0.26667, 0.33333
        )
        assertEquals(expectedInverse, matrix.inverse())
    }

    @Test
    fun testMultiplyProductByItsInverse() {
        val matrixA = Matrix(
            Dim(4, 4),
            3.0, -9.0, 7.0, 3.0,
            3.0, -8.0, 2.0, -9.0,
            -4.0, 4.0, 4.0, 1.0,
            -6.0, 5.0, -1.0, 1.0
        )
        val matrixB = Matrix(
            Dim(4, 4),
            8.0, 2.0, 2.0, 2.0,
            3.0, -1.0, 7.0, 0.0,
            7.0, 0.0, 5.0, 4.0,
            6.0, -2.0, 0.0, 5.0
        )
        assertEquals(matrixA, matrixA * matrixB * matrixB.inverse())
    }

}
