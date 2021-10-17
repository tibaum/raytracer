package raytracer.elements

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TupleTest {

    @Test
    fun testPoint() {
        val tuple = Tuple(4.3, -4.2, 3.1, 1.0)
        assertEquals(4.3, tuple.values[0])
        assertEquals(-4.2, tuple.values[1])
        assertEquals(3.1, tuple.values[2])
        assertEquals(1.0, tuple.values[3])
        assertTrue(tuple.isPoint())
        assertFalse(tuple.isVector())
    }

    @Test
    fun testVector() {
        val tuple = Tuple(4.3, -4.2, 3.1, 0.0)
        assertEquals(4.3, tuple.values[0])
        assertEquals(-4.2, tuple.values[1])
        assertEquals(3.1, tuple.values[2])
        assertEquals(0.0, tuple.values[3])
        assertFalse(tuple.isPoint())
        assertTrue(tuple.isVector())
    }

    @Test
    fun testCreatePoint() {
        val point = Tuple.createPoint(4.0, -4.0, 3.0)
        assertEquals(Tuple(4.0, -4.0, 3.0, 1.0), point)
    }

    @Test
    fun testCreateVector() {
        val vector = Tuple.createVector(4.0, -4.0, 3.0)
        assertEquals(Tuple(4.0, -4.0, 3.0, 0.0), vector)
    }

    @Test
    fun testAddition() {
        val point = Tuple.createPoint(3.0, -2.0, 5.0)
        val vector = Tuple.createVector(-2.0, 3.0, 1.0)
        assertEquals(Tuple(1.0, 1.0, 6.0, 1.0), point + vector)
    }

    @Test
    fun testSubtractionOfTwoPoints() {
        val point1 = Tuple.createPoint(3.0, 2.0, 1.0)
        val point2 = Tuple.createPoint(5.0, 6.0, 7.0)
        assertEquals(Tuple(-2.0, -4.0, -6.0, 0.0), point1 - point2)
    }

    @Test
    fun testSubtractVectorFromPoint() {
        val point = Tuple.createPoint(3.0, 2.0, 1.0)
        val vector = Tuple.createVector(5.0, 6.0, 7.0)
        assertEquals(Tuple(-2.0, -4.0, -6.0, 1.0), point - vector)
    }

    @Test
    fun testSubtractVectorFromVector() {
        val vector1 = Tuple.createVector(3.0, 2.0, 1.0)
        val vector2 = Tuple.createVector(5.0, 6.0, 7.0)
        assertEquals(Tuple(-2.0, -4.0, -6.0, 0.0), vector1 - vector2)
    }

    @Test
    fun testNegate() {
        val tuple = Tuple(1.0, -2.0, 3.0, -4.0)
        assertEquals(Tuple(-1.0, 2.0, -3.0, 4.0), -tuple)
    }

    @Test
    fun testMultiplyTupleWithScalar() {
        val tuple = Tuple(1.0, -2.0, 3.0, -4.0)
        assertEquals(Tuple(3.5, -7.0, 10.5, -14.0), tuple * 3.5)
    }

    @Test
    fun testMultiplyTupleWithFraction() {
        val tuple = Tuple(1.0, -2.0, 3.0, -4.0)
        assertEquals(Tuple(0.5, -1.0, 1.5, -2.0), tuple * 0.5)
    }

    @Test
    fun testDivideTupleByScalar() {
        val tuple = Tuple(1.0, -2.0, 3.0, -4.0)
        assertEquals(Tuple(0.5, -1.0, 1.5, -2.0), tuple / 2.0)
    }

    @Test
    fun testMagnitudeWithOnlyX() {
        val vector = Tuple.createVector(1.0, 0.0, 0.0)
        assertEquals(1.0, vector.magnitude())
    }

    @Test
    fun testMagnitudeWithOnlyY() {
        val vector= Tuple.createVector(0.0, 1.0, 0.0)
        assertEquals(1.0, vector.magnitude())
    }

    @Test
    fun testMagnitudeWithOnlyZ() {
        val vector = Tuple.createVector(0.0, 0.0, 1.0)
        assertEquals(1.0, vector.magnitude())
    }

    @Test
    fun testMagnitude() {
        val vector = Tuple.createVector(-1.0, -2.0, -3.0)
        assertEquals(3.7416573867739413, vector.magnitude())
    }

    @Test
    fun testNormalizeTupleWithOnlyX() {
        val vector = Tuple.createVector(4.0, 0.0, 0.0)
        assertEquals(Tuple(1.0, 0.0, 0.0), vector.normalize())
    }

    @Test
    fun testNormalizeTuple() {
        val vector = Tuple.createVector(1.0, 2.0, 3.0)
        assertEquals(Tuple(0.26726, 0.53452, 0.80178), vector.normalize())
    }

    @Test
    fun testNormalizeTupleAndCalculateMagnitude() {
        val vector = Tuple.createVector(1.0, 2.0, 3.0)
        val normalizedVector = vector.normalize()
        assertEquals(1.0, normalizedVector.magnitude())
    }

    @Test
    fun testDotProduct() {
        val vector1 = Tuple.createVector(1.0, 2.0, 3.0)
        val vector2 = Tuple.createVector(2.0, 3.0, 4.0)
        assertEquals(20.0, vector1.dot(vector2))
    }

    @Test
    fun testCrossProduct() {
        val vector1 = Tuple.createVector(1.0, 2.0, 3.0)
        val vector2 = Tuple.createVector(2.0, 3.0, 4.0)
        assertEquals(Tuple(-1.0, 2.0, -1.0), vector1.cross(vector2))
        assertEquals(Tuple(1.0, -2.0, 1.0), vector2.cross(vector1))
    }

}

