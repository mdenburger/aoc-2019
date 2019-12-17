package day17

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import shared.IntCode

data class Point(val x: Int, val y: Int) {
    fun isBorder(width: Int, height: Int) = x == 0 || x == width -1 || y == 0 || y == height - 1
    fun around() = sequence {
        yield(Point(x, y - 1)) // north
        yield(Point(x + 1, y)) // east
        yield(Point(x, y + 1)) // south
        yield(Point(x - 1, y)) // west
    }
}

fun main() {
    part1()
    part2();
}

fun part1() {
    val program = IntCode("src/main/kotlin/day17/day17-input.txt")
    val grid = readGrid(program)

    val width = grid.keys.map { it.x }.max()!! + 1
    val height = grid.keys.map { it.y }.max()!! + 1

    val intersections = grid.filter { (point, char) ->
        char == '#' && !point.isBorder(width, height) && point.around().all { grid[it] == '#' }
    }

    val answer1 = intersections.keys.map { it.x * it.y }.sum()
    println("Answer 1: $answer1")
}

private fun readGrid(program: IntCode): MutableMap<Point, Char> {
    val input = Channel<Long>(Channel.UNLIMITED)
    val output = Channel<Long>(Channel.UNLIMITED)

    val grid = runBlocking {
        launch {
            program.run(input, output)
        }
        withContext(Dispatchers.Default) {
            collectGrid(output)
        }
    }

    return grid
}

suspend fun collectGrid(output: Channel<Long>): MutableMap<Point, Char> {
    val grid = linkedMapOf<Point, Char>()
    var y = 0
    var x = 0

    try {
        while (true) {
            val char = output.receive().toChar()
            grid[Point(x, y)] = char

            //print(char)

            when(char) {
                '\n' -> {
                    x = 0
                    y++
                }
                else -> x++
            }
        }
    } catch (e: ClosedReceiveChannelException) {
        return grid
    }
}

fun part2() {
    val program = IntCode("src/main/kotlin/day17/day17-input.txt")

    program.set(0, 2)

    val input = Channel<Long>(Channel.UNLIMITED)
    val output = Channel<Long>(Channel.UNLIMITED)

    runBlocking {
        launch {
            program.run(input, output)
        }
        launch {
            val main = "A,B,A,C,A,B,C,B,C,B\n"
            val a = "L,10,R,8,L,6,R,6\n"
            val b = "L,8,L,8,R,8\n"
            val c = "R,8,L,6,L,10,L,10\n"
            val feed = "n\n"

            main.sendTo(input)
            a.sendTo(input)
            b.sendTo(input)
            c.sendTo(input)
            feed.sendTo(input)

            var last: Long = 0

            try {
                while (true) {
                    last = output.receive()
                    //print(last.toChar())
                }
            } catch (e: ClosedReceiveChannelException) {
                println("Answer 2: $last")
            }
        }
    }
}

suspend fun String.sendTo(channel: Channel<Long>) = forEach { channel.send(it.toLong()) }
