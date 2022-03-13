package raytracer.elements

/**
 * Material encapsulates the surface color and attributes from the Phong reflection model.
 *
 * @param surfaceColor color of the material
 * @param ambientReflection background light or light reflected from other objects in the environment, a nonnegative value, typically between 0 and 1
 * @param diffuseReflection light reflected from a matte surface, a nonnegative value, typically between 0 and 1
 * @param specularReflection reflection of the light source itself, a nonnegative value, typically between 0 and 1
 * @param shininess controls size and tightness of the specular highlight, a nonnegative value, typically between 10 and 200
 * @param reflective value between 0 (nonreflective) and 1 (mirror)
 * @param transparency a value of 0 makes the surface opaque
 * @param refractiveIndex degree to which light will bend when entering or exiting the material, a larger number means stronger refraction
 */
data class Material(
    val surfaceColor: Tuple = Tuple.color(1.0, 1.0, 1.0),
    val ambientReflection: Double = 0.1,
    val diffuseReflection: Double = 0.9,
    val specularReflection: Double = 0.9,
    val shininess: Double = 200.0,
    val pattern: Pattern? = null,
    val reflective: Double = 0.0,
    val transparency: Double = 0.0,
    val refractiveIndex: Double = 1.0
) {

    init {
        require(surfaceColor.isColor())
        require(ambientReflection >= 0.0) { "ambientReflection must be nonnegative" }
        require(diffuseReflection >= 0.0) { "diffuseReflection must be nonnegative" }
        require(specularReflection >= 0.0) { "specularReflection must be nonnegative" }
        require(shininess >= 0.0) { "shininess must be nonnegative" }
        require(reflective in 0.0..1.0) { "reflective must be a number between 0 and 1" }
        require(transparency >= 0.0) { "transparency must be nonnegative" }
        require(refractiveIndex >= 0.0) { "refractiveIndex must be nonnegative" }
    }

}
