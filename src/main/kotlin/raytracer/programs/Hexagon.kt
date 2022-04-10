package raytracer.programs

import raytracer.elements.*
import raytracer.elements.Matrix.Companion.rotationX
import raytracer.elements.Matrix.Companion.rotationY
import raytracer.elements.Matrix.Companion.rotationZ
import raytracer.elements.Matrix.Companion.scaling
import raytracer.elements.Matrix.Companion.translation
import raytracer.elements.Tuple.Companion.color
import raytracer.elements.Tuple.Companion.point
import raytracer.elements.Tuple.Companion.vector
import java.io.File
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.PI

fun main() {
    val timestampStart = LocalDateTime.now()
    println("$timestampStart Starting program")

    val pointLight = PointLight(
        position = point(-10.0, 10.0, -10.0),
        intensity = color(1.0, 1.0, 1.0)
    )

    val world = World(pointLight, listOf(hexagon()))

    val camera = Camera(
        horizontalCanvasSize = 1000,
        verticalCanvasSize = 500,
        fieldOfView = PI / 3,
        transformationMatrix = Matrix.viewTransform(
            from = point(0.0, 1.5, -5.0),
            to = point(0.0, 1.0, 0.0),
            up = vector(0.0, 1.0, 0.0)
        )
    )

    val timestampCalculatingPixels = LocalDateTime.now()
    println("$timestampCalculatingPixels Calculating the pixels of the canvas")

    val image = camera.render(world)

    val timestampWritingToFile = LocalDateTime.now()
    val filename = "hexagon.ppm"
    println("$timestampWritingToFile Writing canvas to file $filename")
    File(filename).writeText(image.toPPM())

    val timestampFinish = LocalDateTime.now()
    val duration = Duration.between(timestampStart, timestampFinish)
    println("$timestampFinish Program took ${duration.toMillis()} milliseconds to complete")
}

fun hexagon() = Group(
    transformationMatrix = rotationY(0.3) * rotationX(-0.3) * translation(0.0, 1.0, 0.0),
    shapes = (0..5).map(::hexagonSide).toList()
)

fun hexagonSide(n: Int) = Group(rotationY(n * PI / 3), listOf(hexagonCorner(), hexagonEdge()))

fun hexagonCorner() = Sphere(transformationMatrix = translation(0.0, 0.0, -1.0) * scaling(0.25, 0.25, 0.25))

fun hexagonEdge() = Cylinder(
    minimum = 0.0,
    maximum = 1.0,
    transformationMatrix = translation(0.0, 0.0, -1.0)
            * rotationY(-PI / 6)
            * rotationZ(-PI / 2)
            * scaling(0.25, 1.0, 0.25)
)
