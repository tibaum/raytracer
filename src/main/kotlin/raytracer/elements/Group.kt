package raytracer.elements

class Group(
    transformationMatrix: Matrix = Matrix.identity(4)
) : Shape(transformationMatrix, Material()) {

    private val shapes = mutableListOf<Shape>()

    fun add(shape: Shape) {
        shape.group = this
        shapes.add(shape)
    }

    override fun localIntersect(ray: Ray): Intersections =
        if (shapes.isEmpty()) Intersections()
        else shapes.map { it.intersect(ray) }.reduce(Intersections::accumulate)


    override fun localNormalAt(point: Tuple): Tuple {
        throw UnsupportedOperationException("call localNormalAt on the concrete shapes in this group")
    }

}
