package raytracer.elements

import raytracer.elements.Tuple.Companion.point
import kotlin.Double.Companion.NEGATIVE_INFINITY
import kotlin.Double.Companion.POSITIVE_INFINITY
import kotlin.math.abs

class Plane(
    transformationMatrix: Matrix = Matrix.identity(4),
    material: Material = Material()
) : Shape(transformationMatrix, material) {

    override fun localIntersect(ray: Ray): Intersections =
        if (abs(ray.direction[1]) < EPSILON) Intersections()
        else Intersections(Intersection(-ray.origin[1] / ray.direction[1], this))

    override fun localNormalAt(point: Tuple, hit: Intersection?): Tuple = Tuple.vector(0.0, 1.0, 0.0)

    override fun localBoundingBox(): BoundingBox =
        BoundingBox(point(NEGATIVE_INFINITY, 0.0, NEGATIVE_INFINITY), point(POSITIVE_INFINITY, 0.0, POSITIVE_INFINITY))

}
