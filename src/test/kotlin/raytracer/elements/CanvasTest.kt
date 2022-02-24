package raytracer.elements

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CanvasTest {

    @Test
    fun testConstruction() {
        val canvas = Canvas(10, 20)
        assertEquals(10, canvas.width)
        assertEquals(20, canvas.height)
        canvas.pixels.forEach { color -> assertEquals(Tuple.color(0.0, 0.0, 0.0), color) }
    }

    @Test
    fun testChangePixel() {
        val canvas = Canvas(10, 20)
        val red = Tuple.color(1.0, 0.0, 0.0)
        canvas.writePixel(2, 3, red)
        assertEquals(red, canvas.pixelAt(2, 3))
    }

    @Test
    fun testPPMColor() {
        val values = PPMColor(Color(1.5, 0.5, -0.5)).getValues()
        assertEquals("255", values[0])
        assertEquals("128", values[1])
        assertEquals("0", values[2])
    }

    @Test
    fun testPPMHeader() {
        val lines = Canvas(5, 3).toPPM().lines()
        assertEquals("P3", lines[0])
        assertEquals("5 3", lines[1])
        assertEquals("255", lines[2])
    }

    @Test
    fun testPPMPixelData() {
        val canvas = Canvas(5, 3)
        canvas.writePixel(0, 0, Color(1.5, 0.0, 0.0))
        canvas.writePixel(2, 1, Color(0.0, 0.5, 0.0))
        canvas.writePixel(4, 2, Color(-0.5, 0.0, 1.0))
        val lines = canvas.toPPM().lines()
        assertEquals("255 0 0 0 0 0 0 0 0 0 0 0 0 0 0", lines[3])
        assertEquals("0 0 0 0 0 0 0 128 0 0 0 0 0 0 0", lines[4])
        assertEquals("0 0 0 0 0 0 0 0 0 0 0 0 0 0 255", lines[5])
    }

    @Test
    fun testPPMSplittingLongLines() {
        val canvas = Canvas(10, 2)
        for (column in 0..9) {
            for (row in 0..1) {
                canvas.writePixel(column, row, Tuple.color(1.0, 0.8, 0.6))
            }
        }
        val lines = canvas.toPPM().lines()
        assertEquals("255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204", lines[3])
        assertEquals("153 255 204 153 255 204 153 255 204 153 255 204 153", lines[4])
        assertEquals("255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204", lines[5])
        assertEquals("153 255 204 153 255 204 153 255 204 153 255 204 153", lines[6])
    }

    @Test
    fun testPPMTerminatesWithNewline() {
        val ppm = Canvas(5, 3).toPPM()
        assertEquals('\n', ppm.last())
    }

}