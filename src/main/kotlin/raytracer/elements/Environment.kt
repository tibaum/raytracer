package raytracer.elements

data class Projectile(val position: Point, val velocity: Vector)

class Environment(private val gravity: Vector, private val wind: Vector) {

    /**
     * Calculates the projectile after one unit of time has passed in this environment.
     */
    fun tick(projectile: Projectile): Projectile {
        val position = projectile.position + projectile.velocity
        val velocity = projectile.velocity + gravity + wind
        return Projectile(position, velocity)
    }

}
