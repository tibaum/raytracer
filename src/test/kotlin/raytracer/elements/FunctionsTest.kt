package raytracer.elements

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class FunctionsTest {

    @Test
    fun almostEqualFalse() {
        assertFalse(almostEqual(0.00001, 0.00002))
        assertFalse(almostEqual(0.00002, 0.00001))

        assertFalse(almostEqual(-0.00001, 0.0))
        assertFalse(almostEqual(0.0, -0.00001))

        assertFalse(almostEqual(-0.00001, -0.00002))
        assertFalse(almostEqual(-0.00002, -0.00001))
    }

    @Test
    fun almostEqualTrue() {
        assertTrue(almostEqual(0.000011, 0.00002))
        assertTrue(almostEqual(0.00002, 0.000011))

        assertTrue(almostEqual(-0.000009, 0.0))
        assertTrue(almostEqual(0.0, -0.000009))

        assertTrue(almostEqual(-0.00002, -0.000011))
        assertTrue(almostEqual(-0.000011, -0.00002))
    }

}
