package raytracer.elements

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class EnvironmentTest {

    @Test
    fun testTick() {
        val gravity = Tuple.vector(0.0, -0.1, 0.0)
        val wind = Tuple.vector(-0.01, 0.0, 0.1)
        val environment = Environment(gravity, wind)

        val position = Tuple.point(2.0, 1.0, 3.0)
        val velocity = Tuple.vector(1.0, 3.0, 1.0)
        val projectile = Projectile(position, velocity)

        val newPosition = Tuple.point(3.0, 4.0, 4.0)
        val newVelocity = Tuple.vector(0.99, 2.9, 1.1)
        val newProjectile = Projectile(newPosition, newVelocity)

        assertEquals(newProjectile, environment.tick(projectile))
    }

}
