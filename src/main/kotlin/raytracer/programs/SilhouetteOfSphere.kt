package raytracer.programs

import raytracer.elements.*
import java.io.File
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.PI

/**
 * Casts rays at a sphere and draws the resulting silhouette on a canvas.
 */
fun main() {
    val timestampStart = LocalDateTime.now()
    println("$timestampStart Starting program")

    val rayOrigin = Tuple.point(0.0, 0.0, -5.0)
    val wallZ = 10.0
    val wallSize = 7.0

    val canvasPixels = 300
    val pixelSize = wallSize / canvasPixels
    val halfWallSize = wallSize / 2.0

    val canvas = Canvas(canvasPixels, canvasPixels)
    val red = Tuple.color(1.0, 0.0, 0.0)
    val sphere = Sphere(Matrix.rotationZ(PI / 4) * Matrix.scaling(0.5, 1.0, 1.0))

    val timestampCalculatingPixels = LocalDateTime.now()
    println("$timestampCalculatingPixels Calculating the pixels of the canvas")

    for (y in 0 until canvasPixels) {
        val worldY = halfWallSize - pixelSize * y
        for (x in 0 until canvasPixels) {
            val worldX = -halfWallSize + pixelSize * x
            val position = Tuple.point(worldX, worldY, wallZ)
            val ray = Ray(rayOrigin, (position - rayOrigin).normalize())
            val intersections = sphere.intersect(ray)
            if (intersections.hit() != null) {
                canvas.writePixel(x, y, red)
            }
        }
    }

    val timestampWritingToFile = LocalDateTime.now()
    val filename = "silhouetteOfSphere.ppm"
    println("$timestampWritingToFile Writing canvas to file $filename")
    File(filename).writeText(canvas.toPPM())

    val timestampFinish = LocalDateTime.now()
    val duration = Duration.between(timestampStart, timestampFinish)
    println("$timestampFinish Program took ${duration.toMillis()} milliseconds to complete")
}
