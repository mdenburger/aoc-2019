package day03.second

import java.io.File

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

class PointInfo(wire: Wire, steps: Int) {
    val wires = arrayListOf(wire)
    var totalSteps = steps

    fun addWire(wire: Wire, steps: Int) {
        wires.add(wire);
        totalSteps += steps;
    }
}

fun main() {
    val grid = linkedMapOf<Point, PointInfo>()
    val intersections = arrayListOf<PointInfo>()

    readWires().forEach { wire ->
        val position = Point(0, 0)
        var steps = 0

        for (pathSegment in wire.segments) {
            repeat(pathSegment.length) {
                position.x += pathSegment.direction.dx
                position.y += pathSegment.direction.dy
                steps += 1

                val info = grid[position]

                if (info == null) {
                    grid[position.copy()] = PointInfo(wire, steps)
                } else if (!info.wires.contains(wire)) {
                    info.addWire(wire, steps)
                    intersections.add(info)
                }
            }
        }
    }

    intersections.sortBy { it.totalSteps }
    println(intersections.first().totalSteps)
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
