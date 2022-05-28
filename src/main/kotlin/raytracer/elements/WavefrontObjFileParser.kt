package raytracer.elements

import raytracer.elements.Tuple.Companion.point
import raytracer.elements.Tuple.Companion.vector
import java.io.File
import java.util.*

class WavefrontObjFileParser(private val file: File) {

    var numbersOfIgnoredLines: Int = 0
        private set

    private val vertices: MutableList<Tuple> = mutableListOf()
    private val vertexNormals: MutableList<Tuple> = mutableListOf()
    private val triangleGroups: MutableMap<String, MutableList<Triangle>> = mutableMapOf()

    fun vertices(): List<Tuple> = Collections.unmodifiableList(vertices)

    fun vertexNormals(): List<Tuple> = Collections.unmodifiableList(vertexNormals)

    fun triangles(groupName: String): List<Triangle> {
        val triangleGroup = triangleGroups[groupName]
        return if (triangleGroup == null) emptyList() else Collections.unmodifiableList(triangleGroup)
    }

    fun parse() {
        var currentGroup = "defaultGroup"
        file.forEachLine { line ->
            val instruction = line.split(" ")
            when (instruction[0]) {
                "g" -> currentGroup = instruction[1]
                "v" -> vertices.add(vertex(instruction))
                "vn" -> vertexNormals.add(vertexNormal(instruction))
                "f" -> triangleGroups.getOrPut(currentGroup) { mutableListOf() }.addAll(triangles(instruction))
                else -> numbersOfIgnoredLines++
            }
        }
    }

    private fun vertex(instruction: List<String>): Tuple =
        point(
            instruction[1].toDouble(),
            instruction[2].toDouble(),
            instruction[3].toDouble()
        )

    private fun vertexNormal(instruction: List<String>): Tuple =
        vector(
            instruction[1].toDouble(),
            instruction[2].toDouble(),
            instruction[3].toDouble()
        )

    private fun triangles(instruction: List<String>): List<Triangle> =
        when (instruction.size) {
            4 -> listOf(triangle(instruction))
            6 -> fanTriangulation()
            else -> emptyList()
        }

    private fun triangle(instruction: List<String>): Triangle =
        if (instruction[1].contains("/")) {
            val vertexData1 = instruction[1].split("/")
            val vertexData2 = instruction[2].split("/")
            val vertexData3 = instruction[3].split("/")
            SmoothTriangle(
                point1 = vertices[vertexData1[0].toInt() - 1],
                point2 = vertices[vertexData2[0].toInt() - 1],
                point3 = vertices[vertexData3[0].toInt() - 1],
                normal1 = vertexNormals[vertexData1[2].toInt() - 1],
                normal2 = vertexNormals[vertexData2[2].toInt() - 1],
                normal3 = vertexNormals[vertexData3[2].toInt() - 1]
            )
        } else Triangle(
            point1 = vertices[instruction[1].toInt() - 1],
            point2 = vertices[instruction[2].toInt() - 1],
            point3 = vertices[instruction[3].toInt() - 1]
        )


    private fun fanTriangulation(): List<Triangle> =
        (1 until vertices.size - 1)
            .map { Triangle(point1 = vertices[0], point2 = vertices[it], point3 = vertices[it + 1]) }
            .toList()

    fun toGroup(): Group = Group(shapes = triangleGroups.map { entry -> Group(shapes = entry.value) })

}
