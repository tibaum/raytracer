package raytracer.elements

import kotlin.math.abs

class Plane(
    transformationMatrix: Matrix = Matrix.identity(4),
    material: Material = Material()
) : Shape(transformationMatrix, material) {

    override fun localIntersect(ray: Ray): Intersections =
        if (abs(ray.direction[1]) < EPSILON) Intersections()
        else Intersections(Intersection(-ray.origin[1] / ray.direction[1], this))

    override fun localNormalAt(point: Tuple): Tuple = Tuple.vector(0.0, 1.0, 0.0)

}
