package day10

import java.io.File
import java.lang.Double.NaN
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.acos
import kotlin.math.sqrt

data class Point(val x: Int, val y: Int) {
    var angle = NaN
}

fun main() {
    val fileName = "src/main/kotlin/day10/day10-input.txt"
    println("Answer 1: " + maxVisiblePoints(fileName))
    println("Answer 2: " + vaporizedAstroid(fileName, 200))
}

fun maxVisiblePoints(fileName: String) = readPoints(fileName).visible().max()

fun readPoints(fileName: String): LinkedHashSet<Point> {
    val points = linkedSetOf<Point>()

    File(fileName).readLines().forEachIndexed { y, line ->
        line.forEachIndexed { x, character ->
            if (character == '#') {
                points.add(Point(x, y))
            }
        }
    }
    return points
}

fun LinkedHashSet<Point>.visible() = map { candidate ->
    map { if (noneBetween(candidate, it)) 1 else 0 }.sum()
}

fun LinkedHashSet<Point>.noneBetween(a: Point, b: Point): Boolean {
    if (a == b) {
        return false
    }

    val dx = (a.x - b.x).absoluteValue
    val stepX = if (a.x < b.x) 1 else -1
    val dy = (a.y - b.y).absoluteValue
    val stepY = if (a.y < b.y) 1 else -1

    when (dx) {
        0 -> {
            for (y in 1 until dy) {
                val cx = a.x
                val cy = a.y + (stepY * y)
                if (contains(Point(cx, cy))) {
                    return false
                }
            }
        }
        else -> {
            for (x in 1 until dx) {
                val cx = a.x + (stepX * x)
                if ((x * dy) % dx == 0) {
                    val cy = a.y + ((x * dy * stepY) / dx)
                    if (contains(Point(cx, cy))) {
                        return false
                    }
                }
            }
        }
    }

    return true
}

fun vaporizedAstroid(fileName: String, astroidNumber: Int): Int {
    val points = readPoints(fileName)
    val laser = points.laser()

    points.forEach { it.angle = angle(laser, it) }

    var count = 0

    while (true) {
        val vaporized = points.filter { points.noneBetween(it, laser) }.sortedBy { it.angle }
        if (count + vaporized.size > astroidNumber - 1) {
            val solution = vaporized[astroidNumber - 1 - count]
            return solution.x * 100 + solution.y
        }
        count += vaporized.size
        points.removeAll(vaporized)
    }
}


fun LinkedHashSet<Point>.laser(): Point {
    val visiblePoints = visible()
    val maxVisible = visiblePoints.max()
    return toList()[visiblePoints.indexOf(maxVisible)]
}

fun angle(center: Point, point: Point): Double {
    val dy1 = 0 - center.y
    val dx2 = point.x - center.x
    val dy2 = point.y - center.y

    val dotProduct = dy1 * dy2
    val productSquaredLengths = (dy1 * dy1) * ((dx2 * dx2) + (dy2 * dy2))

    val radians = acos(dotProduct/ sqrt(productSquaredLengths.toDouble()));

    return if (center.x <= point.x) radians else 2 * PI - radians
}
