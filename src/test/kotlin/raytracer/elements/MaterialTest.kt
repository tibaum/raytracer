package raytracer.elements

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class MaterialTest {

    @Test
    fun testInputValidation() {
        assertDoesNotThrow { Material() }
        assertThrows(IllegalArgumentException::class.java) { Material(surfaceColor = Tuple(2.0, 1.0, 1.0)) }
        assertThrows(IllegalArgumentException::class.java) { Material(ambientReflection = -0.1) }
        assertThrows(IllegalArgumentException::class.java) { Material(diffuseReflection = -0.9) }
        assertThrows(IllegalArgumentException::class.java) { Material(specularReflection = -0.9) }
        assertThrows(IllegalArgumentException::class.java) { Material(shininess = -10.0) }
        assertThrows(IllegalArgumentException::class.java) { Material(reflective = -0.1) }
        assertThrows(IllegalArgumentException::class.java) { Material(reflective = 1.1) }
        assertThrows(IllegalArgumentException::class.java) { Material(transparency = -0.1) }
        assertThrows(IllegalArgumentException::class.java) { Material(refractiveIndex = -0.1) }
    }

}
