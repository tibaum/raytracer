package raytracer.elements

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class PointLightTest {

    @Test
    fun testInputValidation() {
        assertDoesNotThrow {
            PointLight(
                position = Tuple.point(1.0, 3.4, 6.7),
                intensity = Tuple.color(1.0, 1.0, 1.0)
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            PointLight(
                position = Tuple.point(1.0, 3.4, 6.7),
                intensity = Tuple(2.0, 1.0, 1.0)
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            PointLight(
                position = Tuple.vector(1.0, 3.4, 6.7),
                intensity = Tuple.color(1.0, 1.0, 1.0)
            )
        }
    }

}
