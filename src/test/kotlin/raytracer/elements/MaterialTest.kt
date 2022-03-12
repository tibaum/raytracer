package raytracer.elements

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class MaterialTest {

    @Test
    fun testInputValidation() {
        assertDoesNotThrow { Material(Tuple(1.0, 1.0, 1.0), 0.1, 0.9, 0.9, 200.0) }
        assertThrows(IllegalArgumentException::class.java) { Material(Tuple(2.0, 1.0, 1.0), 0.1, 0.9, 0.9, 200.0) }
        assertThrows(IllegalArgumentException::class.java) { Material(Tuple(1.0, 1.0, 1.0), -0.1, 0.9, 0.9, 200.0) }
        assertThrows(IllegalArgumentException::class.java) { Material(Tuple(1.0, 1.0, 1.0), 0.1, -0.9, 0.9, 200.0) }
        assertThrows(IllegalArgumentException::class.java) { Material(Tuple(1.0, 1.0, 1.0), 0.1, 0.9, -0.9, 200.0) }
        assertThrows(IllegalArgumentException::class.java) { Material(Tuple(1.0, 1.0, 1.0), 0.1, 0.9, 0.9, -10.0) }
        assertThrows(IllegalArgumentException::class.java) {
            Material(
                Tuple(1.0, 1.0, 1.0),
                0.1,
                0.9,
                0.9,
                200.0,
                reflective = -0.1
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            Material(
                Tuple(1.0, 1.0, 1.0),
                0.1,
                0.9,
                0.9,
                200.0,
                reflective = 1.1
            )
        }
    }

}
