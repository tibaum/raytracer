package raytracer.programs

import raytracer.elements.*
import java.io.File
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.PI

/**
 * Creates a scene with three spheres and a plane.
 */
fun main() {
    val timestampStart = LocalDateTime.now()
    println("$timestampStart Starting program")

    val floor = Plane(
        transformationMatrix = Matrix.identity(4),
        material = Material(
            surfaceColor = Tuple.color(1.0, 0.9, 0.9),
            ambientReflection = 0.1,
            diffuseReflection = 0.9,
            specularReflection = 0.0,
            shininess = 200.0
        )
    )

    val leftSphere = Sphere(
        transformationMatrix = Matrix.translation(-1.5, 0.33, -0.75)
                * Matrix.scaling(0.33, 0.33, 0.33),
        material = Material(
            surfaceColor = Tuple.color(1.0, 0.8, 0.1),
            ambientReflection = 0.1,
            diffuseReflection = 0.7,
            specularReflection = 0.3,
            shininess = 200.0
        )
    )

    val middleSphere = Sphere(
        transformationMatrix = Matrix.translation(-0.5, 1.0, 0.5),
        material = Material(
            surfaceColor = Tuple.color(0.1, 1.0, 0.5),
            ambientReflection = 0.1,
            diffuseReflection = 0.7,
            specularReflection = 0.3,
            shininess = 200.0
        )
    )

    val rightSphere = Sphere(
        transformationMatrix = Matrix.translation(1.5, 0.5, -0.5)
                * Matrix.scaling(0.5, 0.5, 0.5),
        material = Material(
            surfaceColor = Tuple.color(0.5, 1.0, 0.1),
            ambientReflection = 0.1,
            diffuseReflection = 0.7,
            specularReflection = 0.3,
            shininess = 200.0
        )
    )

    val pointLight = PointLight(
        position = Tuple.point(-10.0, 10.0, -10.0),
        intensity = Tuple.color(1.0, 1.0, 1.0)
    )

    val world = World(pointLight, listOf(floor, middleSphere, rightSphere, leftSphere))

    val camera = Camera(
        horizontalCanvasSize = 100,
        verticalCanvasSize = 50,
        fieldOfView = PI / 3,
        transformationMatrix = Matrix.viewTransform(
            from = Tuple.point(0.0, 1.5, -5.0),
            to = Tuple.point(0.0, 1.0, 0.0),
            up = Tuple.vector(0.0, 1.0, 0.0)
        )
    )

    val timestampCalculatingPixels = LocalDateTime.now()
    println("$timestampCalculatingPixels Calculating the pixels of the canvas")

    val image = camera.render(world)

    val timestampWritingToFile = LocalDateTime.now()
    val filename = "sceneWithSpheres.ppm"
    println("$timestampWritingToFile Writing canvas to file $filename")
    File(filename).writeText(image.toPPM())

    val timestampFinish = LocalDateTime.now()
    val duration = Duration.between(timestampStart, timestampFinish)
    println("$timestampFinish Program took ${duration.toMillis()} milliseconds to complete")
}
