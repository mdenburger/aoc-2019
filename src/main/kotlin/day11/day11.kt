package day11

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import shared.IntCode

data class Point(val x: Long, val y: Long) {
    fun neighbor(direction: Direction) = when (direction) {
        Direction.UP -> Point(x, y - 1)
        Direction.RIGHT -> Point(x + 1, y)
        Direction.DOWN -> Point(x, y + 1)
        Direction.LEFT -> Point(x - 1, y)
    }
}

enum class Turn { LEFT, RIGHT;
    companion object {
        fun fromLong(long: Long) = when (long) {
            0L -> LEFT
            else -> RIGHT
        }
    }
}

enum class Direction { UP, RIGHT, DOWN, LEFT;
    fun change(turn: Turn) = when (turn) {
        Turn.LEFT -> values()[if (ordinal == 0) values().size - 1 else ordinal - 1]
        Turn.RIGHT -> values()[(ordinal + 1) % values().size]
    }
}

class Robot {
    var point = Point(0, 0)
    var direction = Direction.UP

    fun move(turn: Turn) {
        direction = direction.change(turn)
        point = point.neighbor(direction)
    }
}

fun main() {
    println("Answer 1: " + paint(0).size)
    println("Answer 2: ").also { paint(1).printGrid() }
}

fun paint(initialColor: Long): HashMap<Point, Long> {
    val program = IntCode.fromFile("src/main/kotlin/day11/day11-input.txt")

    val grid = hashMapOf<Point, Long>()
    val robot = Robot()

    runBlocking {
        val input = Channel<Long>(1)
        val output = Channel<Long>(2)

        launch {
            program.run(input, output)
        }
        launch {
            while (!output.isClosedForReceive) {
                val paintColor = output.receive()
                val turn = Turn.fromLong(output.receive())

                grid[robot.point] = paintColor
                robot.move(turn)

                val nextColor = grid.getOrDefault(robot.point, 0)

                input.send(nextColor)
            }
            input.close()
        }

        input.send(initialColor)
    }

    return grid
}

private fun HashMap<Point, Long>.printGrid() {
    val width = map { it.key.x }.max() ?: 0
    val height = map { it.key.y }.max() ?: 0

    for (y in 0..height.toInt()) {
        for (x in 0..width.toInt()) {
            val point = Point(x.toLong(), y.toLong())
            val color = getOrDefault(point, 0)
            val block = if (color == 1L) "\u2588\u2588" else "  "
            print(block)
        }
        println()
    }
}
