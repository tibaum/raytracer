package raytracer.programs

import raytracer.elements.*
import java.io.File
import java.time.Duration
import java.time.LocalDateTime

/**
 * Casts rays at a sphere and calculates the shading to create a 3D effect.
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

    val canvas = Canvas(width = canvasPixels, height = canvasPixels)

    val sphere = Sphere(
        transformationMatrix = Matrix.identity(4),
        material = Material(
            surfaceColor = Tuple.color(1.0, 0.2, 1.0),
            ambientReflection = 0.1,
            diffuseReflection = 0.9,
            specularReflection = 0.9,
            shininess = 200.0
        )
    )

    val pointLight = PointLight(
        position = Tuple.point(-10.0, 10.0, -10.0),
        intensity = Tuple.color(1.0, 1.0, 1.0)
    )

    val timestampCalculatingPixels = LocalDateTime.now()
    println("$timestampCalculatingPixels Calculating the pixels of the canvas")

    for (y in 0 until canvasPixels) {
        val worldY = halfWallSize - pixelSize * y
        for (x in 0 until canvasPixels) {
            val worldX = -halfWallSize + pixelSize * x
            val position = Tuple.point(worldX, worldY, wallZ)
            val ray = Ray(rayOrigin, (position - rayOrigin).normalize())
            val intersections = ray.intersect(sphere)
            if (intersections.hit() != null) {
                val hit = intersections.hit()!!
                val pointWhereRayHitsSphere = ray.position(hit.time)
                val color = hit.sphere.lightning(
                    pointLight = pointLight,
                    illuminatedPoint = pointWhereRayHitsSphere,
                    eyeVector = -ray.direction,
                    normalVector = hit.sphere.normalAt(pointWhereRayHitsSphere),
                    inShadow = false
                )
                canvas.writePixel(x, y, color)
            }
        }
    }

    val timestampWritingToFile = LocalDateTime.now()
    val filename = "sphere3D.ppm"
    println("$timestampWritingToFile Writing canvas to file $filename")
    File(filename).writeText(canvas.toPPM())

    val timestampFinish = LocalDateTime.now()
    val duration = Duration.between(timestampStart, timestampFinish)
    println("$timestampFinish Program took ${duration.toMillis()} milliseconds to complete")
}
