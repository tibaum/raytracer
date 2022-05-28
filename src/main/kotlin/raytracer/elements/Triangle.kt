package raytracer.elements

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

open class Triangle(
    transformationMatrix: Matrix = Matrix.identity(4),
    material: Material = Material(),
    val point1: Tuple,
    val point2: Tuple,
    val point3: Tuple
) : Shape(transformationMatrix, material) {

    private val edge1: Tuple = point2 - point1
    private val edge2: Tuple = point3 - point1
    private val normal: Tuple = edge2.cross(edge1).normalize()

    init {
        require(point1.isPoint()) { "point1 must be a point but was: $point1" }
        require(point2.isPoint()) { "point2 must be a point but was: $point2" }
        require(point3.isPoint()) { "point3 must be a point but was: $point3" }
    }

    override fun localIntersect(ray: Ray): Intersections {
        val directionCrossEdge2 = ray.direction.cross(edge2)
        val determinant = edge1.dot(directionCrossEdge2)
        if (abs(determinant) < EPSILON) return Intersections()

        val f = 1.0 / determinant
        val point1ToOrigin = ray.origin - point1
        val u = f * point1ToOrigin.dot(directionCrossEdge2)
        if (u !in 0.0..1.0) return Intersections()

        val originCrossEdge1 = point1ToOrigin.cross(edge1)
        val v = f * ray.direction.dot(originCrossEdge1)
        if (v < 0 || u + v > 1) return Intersections()

        val t = f * edge2.dot(originCrossEdge1)
        return Intersections(Intersection(t, this, u, v))
    }

    override fun localNormalAt(point: Tuple, hit: Intersection?): Tuple = normal

    override fun localBoundingBox(): BoundingBox {
        val min = point1.elementWise(point2, ::min).elementWise(point3, ::min)
        val max = point1.elementWise(point2, ::max).elementWise(point3, ::max)
        return BoundingBox(min, max)
    }

}
