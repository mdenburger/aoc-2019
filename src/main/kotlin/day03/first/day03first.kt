package day03.first

import java.io.File
import kotlin.math.absoluteValue

enum class Direction(val dx: Int, val dy: Int) {
    U(0, -1),
    D(0, 1),
    L(-1, 0),
    R(1, 0)
}

data class WireSegment(val direction: Direction, val length: Int)

class Wire {
    val segments = arrayListOf<WireSegment>()
}

data class Point(var x: Int, var y: Int)
fun Point.manhattanDistance() = x.absoluteValue + y.absoluteValue

fun main() {
    val grid = linkedMapOf<Point, MutableList<Wire>>()
    val intersections = arrayListOf<Point>()

    readWires().forEach { wire ->
        val position = Point(0, 0)

        for (pathSegment in wire.segments) {
            repeat(pathSegment.length) {
                position.x += pathSegment.direction.dx
                position.y += pathSegment.direction.dy

                val wires = grid[position]

                if (wires == null) {
                    grid[position.copy()] = arrayListOf(wire)
                } else if (!wires.contains(wire)) {
                    intersections.add(position.copy())
                    wires.add(wire)
                }
            }
        }
    }

    println(intersections.map(Point::manhattanDistance).min())
}

private fun readWires(): List<Wire> {
    val wires = arrayListOf<Wire>()

    File("src/main/kotlin/day03/day03-input.txt").forEachLine {
        val wire = Wire()

        it.split(",").forEach { segment ->
            val direction = Direction.valueOf(segment[0].toString())
            val length = segment.substring(1).toInt()
            wire.segments.add(WireSegment(direction, length))
        }

        wires.add(wire)
    }

    return wires
}
