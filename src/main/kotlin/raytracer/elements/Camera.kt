package raytracer.elements

import kotlin.math.tan

/**
 * Maps the three-dimensional scene onto a two-dimensional canvas.
 *
 * @param fieldOfView controls how much the view is zoomed in
 * @param transformationMatrix describes how the world is oriented relative to the camera
 */
class Camera(
    private val horizontalCanvasSize: Int,
    private val verticalCanvasSize: Int,
    fieldOfView: Double,
    transformationMatrix: Matrix = Matrix.identity(4)
) {

    private val inverseTransformation = transformationMatrix.inverse()
    private val halfWidth: Double
    private val halfHeight: Double
    private val pixelSize: Double

    init {
        val halfView = tan(fieldOfView / 2)
        val aspect = horizontalCanvasSize.toDouble() / verticalCanvasSize
        halfWidth = if (aspect >= 1) halfView else halfView * aspect
        halfHeight = if (aspect >= 1) halfView / aspect else halfView
        pixelSize = (halfWidth * 2) / horizontalCanvasSize
    }

    fun rayForPixel(xCoordinate: Int, yCoordinate: Int): Ray {
        val xOffset = (xCoordinate + 0.5) * pixelSize
        val yOffset = (yCoordinate + 0.5) * pixelSize
        val worldX = halfWidth - xOffset
        val worldY = halfHeight - yOffset
        val pixel = inverseTransformation * Tuple.point(worldX, worldY, -1.0)
        val origin = inverseTransformation * Tuple.point(0.0, 0.0, 0.0)
        val direction = (pixel - origin).normalize()
        return Ray(origin, direction)
    }

    fun render(world: World): Canvas {
        val image = Canvas(horizontalCanvasSize, verticalCanvasSize)
        for (y in 0 until verticalCanvasSize) {
            for (x in 0 until horizontalCanvasSize) {
                val ray = rayForPixel(x, y)
                val color = world.colorAtIntersection(ray)
                image.writePixel(x, y, color)
            }
        }
        return image
    }

}
