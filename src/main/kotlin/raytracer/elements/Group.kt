package raytracer.elements

import raytracer.elements.Tuple.Companion.origin

class Group(
    transformationMatrix: Matrix = Matrix.identity(4),
    private val shapes: List<Shape> = emptyList()
) : Shape(transformationMatrix, Material()) {

    private val boundingBox: BoundingBox

    init {
        shapes.forEach { it.group = this }
        boundingBox = localBoundingBox()
    }

    override fun localIntersect(ray: Ray): Intersections =
        if (shapes.isEmpty() || !boundingBox.intersect(ray)) Intersections()
        else shapes.map { it.intersect(ray) }.reduce(Intersections::accumulate)

    override fun localNormalAt(point: Tuple, hit: Intersection?): Tuple {
        throw UnsupportedOperationException("call localNormalAt on the concrete shapes in this group")
    }

    override fun localBoundingBox(): BoundingBox =
        if (shapes.isEmpty()) BoundingBox(origin, origin)
        else shapes.map(Shape::boundingBox).reduce(BoundingBox::accumulate)

    override fun includes(shape: Shape): Boolean =
        (shape is Group && this == shape) || shapes.any { it.includes(shape) }

}
