package raytracer.elements

class Intersections(private vararg val intersections: Intersection) : Iterable<Intersection> {

    init {
        intersections.sort()
    }

    val count: Int
        get() = intersections.size

    operator fun get(index: Int): Intersection {
        return intersections[index]
    }

    /**
     * Computes the intersection which is visible from the ray's origin.
     */
    fun hit(): Intersection? = intersections.find { it.time >= 0 }

    fun accumulate(other: Intersections) = Intersections(*intersections, *other.intersections)

    override fun iterator(): Iterator<Intersection> = intersections.iterator()

}
