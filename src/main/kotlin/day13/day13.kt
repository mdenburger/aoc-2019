package day13

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import shared.IntCode
import java.util.HashMap

enum class Tile { EMPTY, WALL, BLOCK, PADDLE, BALL }

data class Point(val x: Long, val y: Long)

fun main() {
    part1()
    part2()
}

private fun part1() {
    val program = IntCode.fromFile("src/main/kotlin/day13/day13-input.txt")
    val grid = hashMapOf<Point, Tile>()
    runGame(program, grid)
    println("Answer 1: " + grid.values.filter { it == Tile.BLOCK }.count())
}

private fun part2() {
    val program = IntCode.fromFile("src/main/kotlin/day13/day13-input.txt")
    val grid = hashMapOf<Point, Tile>()
    program[0] = 2  // play for free
    val score = runGame(program, grid)
    println("Answer 2: $score")
}

private fun runGame(program: IntCode, grid: HashMap<Point, Tile>): Long {
    val input = Channel<Long>(UNLIMITED)
    val output = Channel<Long>(UNLIMITED)

    val score = runBlocking {
        launch {
            program.run(input, output)
        }
        withContext(Dispatchers.Default) {
            grid.runIO(input, output)
        }
    }
    return score
}

private suspend fun HashMap<Point, Tile>.runIO(input: Channel<Long>, output: Channel<Long>): Long {
    var score = 0L
    var paddleX: Long? = null

    try {
        input.send(0)

        while (true) {
            val x = output.receive()
            val y = output.receive()
            val value = output.receive()

            if (x == -1L && y == 0L) {
                score = value
            } else {
                val tile = Tile.values()[value.toInt()]
                this[Point(x, y)] = tile

                if (tile == Tile.PADDLE) {
                    paddleX = x
                }

                if (paddleX != null && tile == Tile.BALL) {
                    val diffX = (x - paddleX).coerceIn(-1, 1)
                    input.send(diffX)
                }
            }

            //printScreen()
        }
    } catch (e: ClosedReceiveChannelException) {
        // ignore
    }

    return score
}

private fun HashMap<Point, Tile>.printScreen() {
    val width = map { it.key.x }.max() ?: 0
    val height = map { it.key.y }.max() ?: 0

    for (y in 0..height.toInt()) {
        for (x in 0..width.toInt()) {
            val point = Point(x.toLong(), y.toLong())
            val tile = when (getOrDefault(point, Tile.EMPTY)) {
                Tile.EMPTY -> " "
                Tile.WALL -> "\u2588"
                Tile.BLOCK -> "#"
                Tile.PADDLE -> "="
                Tile.BALL -> "O"
            }
            print(tile)
        }
        println()
    }
    println("-".repeat(80))
}
