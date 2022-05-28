package raytracer.elements

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import raytracer.elements.Tuple.Companion.point
import raytracer.elements.Tuple.Companion.vector
import java.io.File
import kotlin.test.assertTrue

internal class WavefrontObjFileParserTest {

    @Test
    fun testIgnoreUnrecognizedLines() {
        val file = File("src/test/resources/gibberish.obj")
        val parser = WavefrontObjFileParser(file)
        parser.parse()
        assertEquals(5, parser.numbersOfIgnoredLines)
    }

    @Test
    fun testRecognizeVertex() {
        val file = File("src/test/resources/vertex-records.obj")
        val parser = WavefrontObjFileParser(file)
        parser.parse()
        val vertices = parser.vertices()
        assertEquals(0, parser.numbersOfIgnoredLines)
        assertEquals(4, vertices.size)
        assertEquals(point(-1.0, 1.0, 0.0), vertices[0])
        assertEquals(point(-1.0, 0.5, 0.0), vertices[1])
        assertEquals(point(1.0, 0.0, 0.0), vertices[2])
        assertEquals(point(1.0, 1.0, 0.0), vertices[3])
    }

    @Test
    fun testRecognizeTriangles() {
        val file = File("src/test/resources/triangle-records.obj")
        val parser = WavefrontObjFileParser(file)
        parser.parse()
        val vertices = parser.vertices()
        val triangles = parser.triangles("defaultGroup")
        assertEquals(1, parser.numbersOfIgnoredLines)
        assertEquals(4, vertices.size)
        assertEquals(2, triangles.size)

        assertEquals(vertices[0], triangles[0].point1)
        assertEquals(vertices[1], triangles[0].point2)
        assertEquals(vertices[2], triangles[0].point3)

        assertEquals(vertices[0], triangles[1].point1)
        assertEquals(vertices[2], triangles[1].point2)
        assertEquals(vertices[3], triangles[1].point3)
    }

    @Test
    fun testRecognizePolygons() {
        val file = File("src/test/resources/polygon-records.obj")
        val parser = WavefrontObjFileParser(file)
        parser.parse()
        val vertices = parser.vertices()
        val triangles = parser.triangles("defaultGroup")
        assertEquals(1, parser.numbersOfIgnoredLines)
        assertEquals(5, vertices.size)
        assertEquals(3, triangles.size)

        assertEquals(vertices[0], triangles[0].point1)
        assertEquals(vertices[1], triangles[0].point2)
        assertEquals(vertices[2], triangles[0].point3)

        assertEquals(vertices[0], triangles[1].point1)
        assertEquals(vertices[2], triangles[1].point2)
        assertEquals(vertices[3], triangles[1].point3)

        assertEquals(vertices[0], triangles[2].point1)
        assertEquals(vertices[3], triangles[2].point2)
        assertEquals(vertices[4], triangles[2].point3)
    }

    @Test
    fun testTrianglesInGroups() {
        val file = File("src/test/resources/triangle-groups.obj")
        val parser = WavefrontObjFileParser(file)
        parser.parse()
        val vertices = parser.vertices()
        val triangleDefaultGroup = parser.triangles("defaultGroup")
        val triangleFirstGroup = parser.triangles("FirstGroup")
        val triangleSecondGroup = parser.triangles("SecondGroup")
        assertEquals(1, parser.numbersOfIgnoredLines)
        assertEquals(4, vertices.size)
        assertEquals(1, triangleFirstGroup.size)
        assertEquals(1, triangleSecondGroup.size)
        assertEquals(0, triangleDefaultGroup.size)

        assertEquals(vertices[0], triangleFirstGroup[0].point1)
        assertEquals(vertices[1], triangleFirstGroup[0].point2)
        assertEquals(vertices[2], triangleFirstGroup[0].point3)

        assertEquals(vertices[0], triangleSecondGroup[0].point1)
        assertEquals(vertices[2], triangleSecondGroup[0].point2)
        assertEquals(vertices[3], triangleSecondGroup[0].point3)
    }

    @Test
    fun testExportGroup() {
        val file = File("src/test/resources/triangle-groups.obj")
        val parser = WavefrontObjFileParser(file)
        parser.parse()
        val triangleFirstGroup = parser.triangles("FirstGroup")
        val triangleSecondGroup = parser.triangles("SecondGroup")
        val group = parser.toGroup()
        assertEquals(group, triangleFirstGroup[0].group?.group)
        assertEquals(group, triangleSecondGroup[0].group?.group)
        assertEquals(point(-1.0, 0.0, 0.0), group.boundingBox().min)
        assertEquals(point(1.0, 1.0, 0.0), group.boundingBox().max)
    }

    @Test
    fun testRecognizeVertexNormals() {
        val file = File("src/test/resources/vertex-normal-records.obj")
        val parser = WavefrontObjFileParser(file)
        parser.parse()

        val vertexNormals = parser.vertexNormals()
        assertEquals(3, vertexNormals.size)
        assertEquals(vector(0.0, 0.0, 1.0), vertexNormals[0])
        assertEquals(vector(0.707, 0.0, -0.707), vertexNormals[1])
        assertEquals(vector(1.0, 2.0, 3.0), vertexNormals[2])
    }

    @Test
    fun testRecognizeFacesWithNormals() {
        val file = File("src/test/resources/faces-with-normals-records.obj")
        val parser = WavefrontObjFileParser(file)
        parser.parse()

        val vertices = parser.vertices()
        val vertexNormals = parser.vertexNormals()
        val triangleDefaultGroup = parser.triangles("defaultGroup")
        assertEquals(2, parser.numbersOfIgnoredLines)
        assertEquals(3, vertices.size)
        assertEquals(3, vertexNormals.size)
        assertEquals(2, triangleDefaultGroup.size)

        assertTrue { triangleDefaultGroup[0] is SmoothTriangle }
        assertTrue { triangleDefaultGroup[1] is SmoothTriangle }

        val triangle1 = triangleDefaultGroup[0] as SmoothTriangle
        assertEquals(vertices[0], triangle1.point1)
        assertEquals(vertices[1], triangle1.point2)
        assertEquals(vertices[2], triangle1.point3)
        assertEquals(vertexNormals[2], triangle1.normal1)
        assertEquals(vertexNormals[0], triangle1.normal2)
        assertEquals(vertexNormals[1], triangle1.normal3)

        val triangle2 = triangleDefaultGroup[1] as SmoothTriangle
        assertEquals(vertices[0], triangle2.point1)
        assertEquals(vertices[1], triangle2.point2)
        assertEquals(vertices[2], triangle2.point3)
        assertEquals(vertexNormals[2], triangle2.normal1)
        assertEquals(vertexNormals[0], triangle2.normal2)
        assertEquals(vertexNormals[1], triangle2.normal3)
    }

}
