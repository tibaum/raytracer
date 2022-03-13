package raytracer.elements

import kotlin.math.pow
import kotlin.math.sqrt

/**
 * This sphere is represented as the unit sphere, i.e., it has its center at the
 * origin of the coordinate system and it has a radius of 1, transformed by a
 * transformation matrix. This way, rays cast at the sphere can be transformed
 * by the inverse of the transformation matrix and cast at the unit sphere.
 */
class Sphere(
    transformationMatrix: Matrix = Matrix.identity(4),
    material: Material = Material()
) : Shape(transformationMatrix, material) {

    override fun localIntersect(ray: Ray): Intersections {
        val sphereToRay = ray.origin - center
        val a = ray.direction.dot(ray.direction)
        val b = 2 * ray.direction.dot(sphereToRay)
        val c = sphereToRay.dot(sphereToRay) - 1
        val discriminant = b.pow(2) - 4 * a * c
        return if (discriminant < 0) Intersections() else {
            val t1 = (-b - sqrt(discriminant)) / (2 * a)
            val t2 = (-b + sqrt(discriminant)) / (2 * a)
            val intersection1 = Intersection(t1, this)
            val intersection2 = Intersection(t2, this)
            Intersections(intersection1, intersection2)
        }
    }

    override fun localNormalAt(point: Tuple): Tuple = point - center

    companion object {
        fun glass(): Sphere = Sphere(material = Material(transparency = 1.0, refractiveIndex = 1.5))
    }

}
