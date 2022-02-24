package raytracer.programs

import raytracer.elements.Canvas
import raytracer.elements.Environment
import raytracer.elements.Projectile
import raytracer.elements.Tuple
import java.io.File

fun main() {

    val gravity = Tuple.vector(0.0, -0.1, 0.0)
    val wind = Tuple.vector(-0.01, 0.0, 0.0)
    val environment = Environment(gravity, wind)

    val startPosition = Tuple.point(0.0, 1.0, 0.0)
    val velocity = Tuple.vector(1.0, 1.0, 0.0).normalize() * 11.25
    var projectile = Projectile(startPosition, velocity)

    val canvas = Canvas(width = 900, height = 500)

    while (projectile.position[1] > 0.0001) {
        projectile = environment.tick(projectile)
        val x = projectile.position[0].toInt()
        val y = projectile.position[1].toInt()
        if (x < canvas.width && y < canvas.height)
            canvas.writePixel(x, canvas.height - y, Tuple.color(1.0, 0.0, 0.0))
    }

    File("projectiles.ppm").writeText(canvas.toPPM())

}
