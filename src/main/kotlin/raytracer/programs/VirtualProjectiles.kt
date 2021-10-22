package raytracer.programs

import raytracer.elements.*
import java.io.File

fun main() {

    val gravity = Tuple.createVector(0.0, -0.1, 0.0)
    val wind = Tuple.createVector(-0.01, 0.0, 0.0)
    val environment = Environment(gravity, wind)

    val startPosition = Tuple.createPoint(0.0, 1.0, 0.0)
    val velocity = Tuple.createVector(1.0, 1.0, 0.0).normalize() * 11.25
    var projectile = Projectile(startPosition, velocity)

    val canvas = Canvas(width = 900, height = 500)

    while (projectile.position[1] > 0.0001) {
        projectile = environment.tick(projectile)
        val x = projectile.position[0].toInt()
        val y = projectile.position[1].toInt()
        if (x < canvas.width && y < canvas.height)
            canvas.writePixel(x, canvas.height - y, Color(1.0, 0.0, 0.0))
    }

    File("projectiles.ppm").writeText(canvas.toPPM())

}
