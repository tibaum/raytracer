package raytracer.elements

class Intersections(private vararg val intersections: Intersection) {

    val count: Int
        get() = intersections.size

    operator fun get(index: Int): Intersection {
        return intersections[index]
    }

}
