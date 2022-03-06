package raytracer.elements

class World(
    private val pointLight: PointLight = PointLight(
        position = Tuple.point(-10.0, 10.0, -10.0),
        intensity = Tuple.color(1.0, 1.0, 1.0)
    ),
    val spheres: List<Sphere> = listOf(
        Sphere(
            transformationMatrix = Matrix.identity(4),
            material = Material(
                surfaceColor = Tuple.color(0.8, 1.0, 0.6),
                ambientReflection = 0.1,
                diffuseReflection = 0.7,
                specularReflection = 0.2,
                shininess = 200.0
            )
        ),
        Sphere(
            transformationMatrix = Matrix.scaling(0.5, 0.5, 0.5),
            material = Material(
                surfaceColor = Tuple.color(1.0, 1.0, 1.0),
                ambientReflection = 0.1,
                diffuseReflection = 0.9,
                specularReflection = 0.9,
                shininess = 200.0
            )
        )
    )
) {

    fun intersect(ray: Ray): Intersections =
        spheres.map { it.intersect(ray) }.reduce(Intersections::accumulate)

    fun shadeHit(computation: ShadingPreComputation): Tuple =
        computation.sphere.lightning(
            pointLight,
            computation.intersectionPoint,
            computation.eyeVector,
            computation.normalVector,
            isShadowed(computation.overPoint)
        )

    fun colorAtIntersection(ray: Ray): Tuple = with(intersect(ray)) {
        if (hit() == null) Tuple.black
        else shadeHit(ShadingPreComputation.of(ray, hit()!!))
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

}
