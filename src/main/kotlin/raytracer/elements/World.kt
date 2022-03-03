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

    fun shadeHit(computation: ShadingPreComputation): Tuple =
        computation.sphere.lightning(
            pointLight,
            computation.intersectionPoint,
            computation.eyeVector,
            computation.normalVector
        )

    fun colorAtIntersection(ray: Ray): Tuple = with(ray.intersect(this)) {
        if (hit() == null) Tuple.black
        else shadeHit(ShadingPreComputation.of(ray, hit()!!))
    }

}
