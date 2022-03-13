package raytracer.elements

data class Intersection(val time: Double, val shape: Shape) : Comparable<Intersection> {

    override fun compareTo(other: Intersection): Int =
        if (time < other.time) -1
        else if (time > other.time) 1
        else 0

}
