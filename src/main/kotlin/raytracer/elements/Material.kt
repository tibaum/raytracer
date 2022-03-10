package raytracer.elements

/**
 * Material encapsulates the surface color and attributes from the Phong reflection model.
 *
 * @param surfaceColor color of the material
 * @param ambientReflection background light or light reflected from other objects in the environment, a nonnegative value, typically between 0 and 1
 * @param diffuseReflection light reflected from a matte surface, a nonnegative value, typically between 0 and 1
 * @param specularReflection reflection of the light source itself, a nonnegative value, typically between 0 and 1
 * @param shininess controls size and tightness of the specular highlight, a nonnegative value, typically between 10 and 200
 */
data class Material(
    val surfaceColor: Tuple = Tuple.color(1.0, 1.0, 1.0),
    val ambientReflection: Double = 0.1,
    val diffuseReflection: Double = 0.9,
    val specularReflection: Double = 0.9,
    val shininess: Double = 200.0,
    val pattern: Pattern? = null
) {

    init {
        require(surfaceColor.isColor())
        require(ambientReflection >= 0.0) { "ambientReflection must be nonnegative" }
        require(diffuseReflection >= 0.0) { "diffuseReflection must be nonnegative" }
        require(specularReflection >= 0.0) { "specularReflection must be nonnegative" }
        require(shininess >= 0.0) { "shininess must be nonnegative" }
    }

}
