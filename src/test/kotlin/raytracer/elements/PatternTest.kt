package raytracer.elements

import org.junit.jupiter.api.Test
import raytracer.elements.Tuple.Companion.point
import kotlin.math.PI
import kotlin.test.assertEquals

class PatternTest {

    @Test
    fun testPatternWithObjectTransformation() {
        val sphere = Sphere(Matrix.scaling(2.0, 2.0, 2.0))
        val pattern = createPattern()
        val color = pattern.patternAtShape(sphere, point(2.0, 3.0, 4.0))
        assertEquals(Tuple(1.0, 1.5, 2.0), color)
    }

    @Test
    fun testPatternWithPatternTransformation() {
        val sphere = Sphere()
        val pattern = createPattern(Matrix.scaling(2.0, 2.0, 2.0))
        val color = pattern.patternAtShape(sphere, point(2.0, 3.0, 4.0))
        assertEquals(Tuple(1.0, 1.5, 2.0), color)
    }

    @Test
    fun testPatternWithBothObjectAndPatternTransformation() {
        val sphere = Sphere(Matrix.scaling(2.0, 2.0, 2.0))
        val pattern = createPattern(Matrix.translation(0.5, 1.0, 1.5))
        val color = pattern.patternAtShape(sphere, point(2.5, 3.0, 3.5))
        assertEquals(Tuple(0.75, 0.5, 0.25), color)
    }

    @Test
    fun testPatternWithGroupTransformation() {
        val group1 = Group(transformationMatrix = Matrix.rotationY(PI / 2))
        val group2 = Group(transformationMatrix = Matrix.scaling(2.0, 2.0, 2.0))
        val sphere = Sphere(transformationMatrix = Matrix.translation(5.0, 0.0, 0.0))
        group1.add(group2)
        group2.add(sphere)
        val pattern = createPattern(Matrix.translation(0.5, 1.0, 1.5))
        val color = pattern.patternAtShape(sphere, point(-2.0, 0.0, -10.0))
        assertEquals(Tuple(-0.5, -1.0, -2.5), color)
    }

    private fun createPattern(transformationMatrix: Matrix = Matrix.identity(4)) =
        object : Pattern(transformationMatrix) {
            override fun patternAt(point: Tuple) = Tuple(point[0], point[1], point[2])
        }

}
