package raytracer.elements

import kotlin.math.roundToInt

class Canvas(
    val width: Int,
    val height: Int,
    val pixels: Array<Color> = Array(width * height) { Color(0.0, 0.0, 0.0) }
) {

    fun pixelAt(column: Int, row: Int) = pixels[index(column, row)]

    fun writePixel(column: Int, row: Int, color: Color) {
        pixels[index(column, row)] = color
    }

    // pixels filled column by column
    private fun index(column: Int, row: Int) = column * height + row

    override fun toString(): String {
        return pixels.joinToString()
    }

    fun toPPM(): String {
        val ppm = StringBuilder()
        ppm.appendLine("P3")
        ppm.appendLine("$width $height")
        ppm.appendLine("255")
        ppm.appendLine(buildPPMRows())
        return ppm.toString()
    }

    private fun buildPPMRows(): String {
        val result = StringBuilder()
        for (row in 0 until height)
            result.appendLine(buildPPMRow(row))
        return result.toString()
    }

    private fun buildPPMRow(row: Int): String {
        val rgbValues: MutableList<String> = mutableListOf()
        for (column in 0 until width)
            rgbValues.addAll(PPMColor(pixelAt(column, row)).getValues())
        return buildLinesWithMaxLength70(rgbValues)
    }

    private fun buildLinesWithMaxLength70(list: List<String>): String {
        var completeLine = ""
        var partLine = ""

        for (i in list.indices) {
            val testLine = if (partLine.isEmpty()) list[i] else partLine + " " + list[i]

            if (testLine.length <= 70) {
                partLine = testLine
            } else {
                completeLine += partLine + "\n"
                partLine = list[i]
            }

            if (i == list.size - 1) {
                completeLine += partLine
            }
        }
        return completeLine
    }

}

class PPMColor(private val color: Color) {

    fun getValues() = colorTPPMDigits()

    private fun colorTPPMDigits(): List<String> {
        val scaledValues = (color * 255.0).values
        val red = cap(scaledValues[0].roundToInt())
        val green = cap(scaledValues[1].roundToInt())
        val blue = cap(scaledValues[2].roundToInt())
        return listOf("$red", "$green", "$blue")
    }

    private fun cap(value: Int) =
        when {
            value < 0 -> 0
            value > 255 -> 255
            else -> value
        }
}


