package day10

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.lang.Math.PI

class day10test {

    @Test
    fun asteroidBetween() {
        val points = readPoints("src/main/kotlin/day10/example1.txt")

        val testPoint = Point(3, 2)

        assertTrue(points.noneBetween(testPoint, Point(1, 0)))
        assertTrue(points.noneBetween(testPoint, Point(4, 0)))

        assertFalse(points.noneBetween(testPoint, Point(0, 2)))
        assertFalse(points.noneBetween(testPoint, Point(1, 2)))
        assertTrue(points.noneBetween(testPoint, Point(2, 2)))
        assertFalse(points.noneBetween(testPoint, Point(3, 2)))
        assertTrue(points.noneBetween(testPoint, Point(4, 2)))

        assertTrue(points.noneBetween(testPoint, Point(4, 3)))

        assertTrue(points.noneBetween(testPoint, Point(3, 4)))
        assertTrue(points.noneBetween(testPoint, Point(4, 4)))
    }

    @Test
    fun example1() {
        assertEquals(8, maxVisiblePoints("src/main/kotlin/day10/example1.txt"))
    }

    @Test
    fun example2() {
        assertEquals(33, maxVisiblePoints("src/main/kotlin/day10/example2.txt"))
    }

    @Test
    fun example3() {
        assertEquals(35, maxVisiblePoints("src/main/kotlin/day10/example3.txt"))
    }

    @Test
    fun example4() {
        assertEquals(41, maxVisiblePoints("src/main/kotlin/day10/example4.txt"))
    }

    @Test
    fun example5() {
        assertEquals(210, maxVisiblePoints("src/main/kotlin/day10/example5.txt"))
    }

    @Test
    fun angles() {
        assertEquals(0.0, angle(Point(4, 2), Point(4, 0)))
        assertEquals(PI / 2, angle(Point(4, 2), Point(6, 2)))
        assertEquals(PI, angle(Point(4, 2), Point(4, 4)))
        assertEquals(PI * 1.5, angle(Point(4, 2), Point(2, 2)))
    }

    @Test
    fun vaporizedExample5() {
        val fileName = "src/main/kotlin/day10/example5.txt"
        assertEquals(1112, vaporizedAstroid(fileName, 1))
        assertEquals(1201, vaporizedAstroid(fileName, 2))
        assertEquals(1202, vaporizedAstroid(fileName, 3))
        assertEquals(1208, vaporizedAstroid(fileName, 10))
        assertEquals(1600, vaporizedAstroid(fileName, 20))
        assertEquals(1609, vaporizedAstroid(fileName, 50))
        assertEquals(1016, vaporizedAstroid(fileName, 100))
        assertEquals(906, vaporizedAstroid(fileName, 199))
        assertEquals(802, vaporizedAstroid(fileName, 200))
        assertEquals(1101, vaporizedAstroid(fileName, 299))
    }
}