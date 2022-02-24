package raytracer.elements

/**
 * A point light is a light source with no size.
 *
 * @param position position of the light source in space
 * @param intensity brightness and color
 */
data class PointLight(
    val position: Tuple = Tuple.point(0.0, 0.0, 0.0),
    val intensity: Tuple = Tuple.color(1.0, 1.0, 1.0)
) {

    init {
        require(position.isPoint())
        require(intensity.isColor())
    }

}
