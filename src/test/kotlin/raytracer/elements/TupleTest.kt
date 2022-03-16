package raytracer.elements

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.sqrt

class TupleTest {

    @Test
    fun testImmutability() {
        val tuple = Tuple(4.3, -4.2, 3.1, 1.0)
        val array = tuple.toDoubleArray()
        array[0] = 9.9
        assertEquals(4.3, tuple[0])
    }

    @Test
    fun testAccessBelowLowerBound() {
        val tuple = Tuple(4.3)
        assertThrows(IndexOutOfBoundsException::class.java) { tuple[-1] }
    }

    @Test
    fun testAccessAboveUpperBound() {
        val tuple = Tuple(4.3)
        assertThrows(IndexOutOfBoundsException::class.java) { tuple[1] }
    }

    @Test
    fun testPoint() {
        val tuple = Tuple(4.3, -4.2, 3.1, 1.0)
        assertEquals(4.3, tuple[0])
        assertEquals(-4.2, tuple[1])
        assertEquals(3.1, tuple[2])
        assertEquals(1.0, tuple[3])
        assertTrue(tuple.isPoint())
        assertFalse(tuple.isVector())
    }

    @Test
    fun testVector() {
        val tuple = Tuple(4.3, -4.2, 3.1, 0.0)
        assertEquals(4.3, tuple[0])
        assertEquals(-4.2, tuple[1])
        assertEquals(3.1, tuple[2])
        assertEquals(0.0, tuple[3])
        assertFalse(tuple.isPoint())
        assertTrue(tuple.isVector())
    }

    @Test
    fun testIsColor() {
        assertTrue(Tuple(1.0, 0.2, 0.1).isColor())
        assertFalse(Tuple(1.0, 0.2).isColor())
        assertFalse(Tuple(1.0, 0.2, 0.1, 0.3).isColor())
        assertFalse(Tuple(0.0, 0.0, -0.01).isColor())
        assertFalse(Tuple(0.0, -0.01, 0.0).isColor())
        assertFalse(Tuple(-0.01, 0.0, 0.0).isColor())
        assertFalse(Tuple(1.01, 1.0, 1.0).isColor())
        assertFalse(Tuple(1.0, 1.01, 1.0).isColor())
        assertFalse(Tuple(1.0, 1.0, 1.01).isColor())
    }

    @Test
    fun testColorCreation() {
        assertDoesNotThrow { Tuple.color(0.0, 0.0, 0.0) }
        assertDoesNotThrow { Tuple.color(1.0, 1.0, 1.0) }
        assertThrows(IllegalArgumentException::class.java) { Tuple.color(1.01, 0.0, 0.0) }
    }

    @Test
    fun testNotEqual() {
        val tuple = Tuple(0.0)
        val tuple2 = Tuple(0.0, 1.0)
        assertNotEquals(tuple, tuple2)
        assertNotEquals(tuple2, tuple)
    }

    @Test
    fun testAlmostEqual() {
        val tuple = Tuple(0.0)
        val tuple2 = Tuple(0.000001)
        assertEquals(tuple, tuple2)
    }

    @Test
    fun testCreatePoint() {
        val point = Tuple.point(4.0, -4.0, 3.0)
        assertEquals(Tuple(4.0, -4.0, 3.0, 1.0), point)
    }

    @Test
    fun testCreateVector() {
        val vector = Tuple.vector(4.0, -4.0, 3.0)
        assertEquals(Tuple(4.0, -4.0, 3.0, 0.0), vector)
    }

    @Test
    fun testAddition() {
        val point = Tuple.point(3.0, -2.0, 5.0)
        val vector = Tuple.vector(-2.0, 3.0, 1.0)
        assertEquals(Tuple(1.0, 1.0, 6.0, 1.0), point + vector)
    }

    @Test
    fun testSubtractionOfTwoPoints() {
        val point1 = Tuple.point(3.0, 2.0, 1.0)
        val point2 = Tuple.point(5.0, 6.0, 7.0)
        assertEquals(Tuple(-2.0, -4.0, -6.0, 0.0), point1 - point2)
    }

    @Test
    fun testSubtractVectorFromPoint() {
        val point = Tuple.point(3.0, 2.0, 1.0)
        val vector = Tuple.vector(5.0, 6.0, 7.0)
        assertEquals(Tuple(-2.0, -4.0, -6.0, 1.0), point - vector)
    }

    @Test
    fun testSubtractVectorFromVector() {
        val vector1 = Tuple.vector(3.0, 2.0, 1.0)
        val vector2 = Tuple.vector(5.0, 6.0, 7.0)
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
        val vector = Tuple.vector(1.0, 0.0, 0.0)
        assertEquals(1.0, vector.magnitude())
    }

    @Test
    fun testMagnitudeWithOnlyY() {
        val vector = Tuple.vector(0.0, 1.0, 0.0)
        assertEquals(1.0, vector.magnitude())
    }

    @Test
    fun testMagnitudeWithOnlyZ() {
        val vector = Tuple.vector(0.0, 0.0, 1.0)
        assertEquals(1.0, vector.magnitude())
    }

    @Test
    fun testMagnitude() {
        val vector = Tuple.vector(-1.0, -2.0, -3.0)
        assertEquals(3.7416573867739413, vector.magnitude())
    }

    @Test
    fun testNormalizeTupleWithOnlyX() {
        val vector = Tuple.vector(4.0, 0.0, 0.0)
        assertEquals(Tuple(1.0, 0.0, 0.0, 0.0), vector.normalize())
    }

    @Test
    fun testNormalizeTuple() {
        val vector = Tuple.vector(1.0, 2.0, 3.0)
        assertEquals(Tuple(0.26726, 0.53452, 0.80178, 0.0), vector.normalize())
    }

    @Test
    fun testNormalizeTupleAndCalculateMagnitude() {
        val vector = Tuple.vector(1.0, 2.0, 3.0)
        val normalizedVector = vector.normalize()
        assertEquals(1.0, normalizedVector.magnitude())
    }

    @Test
    fun testDotProduct() {
        val vector1 = Tuple.vector(1.0, 2.0, 3.0)
        val vector2 = Tuple.vector(2.0, 3.0, 4.0)
        assertEquals(20.0, vector1.dot(vector2))
    }

    @Test
    fun testCrossProduct() {
        val vector1 = Tuple.vector(1.0, 2.0, 3.0)
        val vector2 = Tuple.vector(2.0, 3.0, 4.0)
        assertEquals(Tuple(-1.0, 2.0, -1.0), vector1.cross(vector2))
        assertEquals(Tuple(1.0, -2.0, 1.0), vector2.cross(vector1))
    }

    @Test
    fun testAsVector() {
        val tuple = Tuple(1.0, 2.0, 3.0, 4.0)
        val vector = tuple.asVector()
        assertEquals(Tuple(1.0, 2.0, 3.0, 4.0), tuple)
        assertEquals(Tuple(1.0, 2.0, 3.0, 0.0), vector)
    }

    @Test
    fun testAsVectorExpectsTupleSize4() {
        assertThrows(IllegalArgumentException::class.java) { Tuple().asVector() }
        assertThrows(IllegalArgumentException::class.java) { Tuple(1.0).asVector() }
        assertThrows(IllegalArgumentException::class.java) { Tuple(1.0, 2.0).asVector() }
        assertThrows(IllegalArgumentException::class.java) { Tuple(1.0, 2.0, 3.0).asVector() }
        assertThrows(IllegalArgumentException::class.java) { Tuple(1.0, 2.0, 3.0, 4.0, 5.0).asVector() }
    }

    @Test
    fun testReflectVectorApproachingAt45Degrees() {
        val vector = Tuple.vector(1.0, -1.0, 0.0)
        val surfaceNormal = Tuple.vector(0.0, 1.0, 0.0)
        assertEquals(Tuple.vector(1.0, 1.0, 0.0), vector.reflectAround(surfaceNormal))
    }

    @Test
    fun testReflectVectorOffSlantedSurface() {
        val vector = Tuple.vector(0.0, -1.0, 0.0)
        val surfaceNormal = Tuple.vector(sqrt(2.0) / 2.0, sqrt(2.0) / 2.0, 0.0)
        assertEquals(Tuple.vector(1.0, 0.0, 0.0), vector.reflectAround(surfaceNormal))
    }

    @Test
    fun testDestructuringComponent1() {
        val tuple = Tuple(1.0, 2.0, 3.0)
        val (x) = tuple
        assertEquals(1.0, x)
    }

    @Test
    fun testDestructuringComponents12() {
        val tuple = Tuple(1.0, 2.0, 3.0)
        val (x, y) = tuple
        assertEquals(1.0, x)
        assertEquals(2.0, y)
    }

    @Test
    fun testDestructuringComponents123() {
        val tuple = Tuple(1.0, 2.0, 3.0)
        val (x, y, z) = tuple
        assertEquals(1.0, x)
        assertEquals(2.0, y)
        assertEquals(3.0, z)
    }

}
