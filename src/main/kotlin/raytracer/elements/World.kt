package raytracer.elements

import raytracer.elements.Tuple.Companion.black

class World(
    private val pointLight: PointLight = PointLight(
        position = Tuple.point(-10.0, 10.0, -10.0),
        intensity = Tuple.color(1.0, 1.0, 1.0)
    ),
    val shapes: List<Shape> = listOf(
        Sphere(
            transformationMatrix = Matrix.identity(4),
            material = Material(
                surfaceColor = Tuple.color(0.8, 1.0, 0.6),
                ambientReflection = 0.1,
                diffuseReflection = 0.7,
                specularReflection = 0.2,
                shininess = 200.0,
                pattern = null,
                reflective = 0.0
            )
        ),
        Sphere(
            transformationMatrix = Matrix.scaling(0.5, 0.5, 0.5),
            material = Material(
                surfaceColor = Tuple.color(1.0, 1.0, 1.0),
                ambientReflection = 0.1,
                diffuseReflection = 0.9,
                specularReflection = 0.9,
                shininess = 200.0,
                pattern = null,
                reflective = 0.0
            )
        )
    )
) {

    private val reflectionCalculationRecursionDepth = 5

    fun intersect(ray: Ray): Intersections =
        shapes.map { it.intersect(ray) }.reduce(Intersections::accumulate)

    fun shadeHit(computation: ShadingPreComputation, remaining: Int = reflectionCalculationRecursionDepth): Tuple {
        val surfaceColor = computation.shape.lightning(
            pointLight,
            computation.intersectionPoint,
            computation.eyeVector,
            computation.normalVector,
            isShadowed(computation.overPoint)
        )
        val reflectedColor = reflectedColor(computation, remaining)
        return surfaceColor + reflectedColor
    }

    fun colorAtIntersection(ray: Ray, remaining: Int = reflectionCalculationRecursionDepth): Tuple =
        with(intersect(ray)) {
            if (hit() == null) black
            else shadeHit(ShadingPreComputation.of(ray, hit()!!), remaining.dec())
        }

    fun isShadowed(point: Tuple): Boolean {
        require(point.isPoint())
        val v = pointLight.position - point
        val distance = v.magnitude()
        val direction = v.normalize()
        val ray = Ray(point, direction)
        val intersections = intersect(ray)
        val hit = intersections.hit()
        return hit != null && hit.time < distance
    }

    fun reflectedColor(
        computation: ShadingPreComputation,
        remaining: Int = reflectionCalculationRecursionDepth
    ): Tuple =
        when {
            remaining == 0 -> black
            computation.shape.material.reflective == 0.0 -> black
            else -> {
                val reflectRay = Ray(computation.overPoint, computation.reflectVector)
                val colorAtIntersection = colorAtIntersection(reflectRay, remaining)
                colorAtIntersection * computation.shape.material.reflective
            }
        }

}
