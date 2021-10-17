package raytracer.elements

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ColorTest {

    @Test
    fun testHadamardProduct() {
        val color1 = Tuple(1.0, 0.2, 0.4)
        val color2 = Tuple(0.9, 1.0, 0.1)
        assertEquals(Tuple(0.9, 0.2, 0.04), color1.hadamardProduct(color2))
    }

}