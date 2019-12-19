package day15

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import shared.IntCode

enum class Direction(val id: Long, val dx: Long, val dy: Long) {
    NORTH(1, 0, -1),
    SOUTH(2, 0, 1),
    WEST(3, -1, 0),
    EAST(4, 1, 0);

    fun opposite() = when(this) {
        NORTH -> SOUTH
        EAST -> WEST
        SOUTH -> NORTH
        WEST -> EAST
    }
}

data class Point(val x: Long, val y: Long) {

    var distance: Long = 0L
    var route: List<Direction> = emptyList()

    fun next(direction: Direction): Point {
        val point = Point(x + direction.dx, y + direction.dy)
        point.distance = distance + 1
        point.route = route.toMutableList().also { it.add(direction) }
        return point
    }
}

val START = Point(0, 0)

val walkable = hashSetOf<Point>()
val walls = hashSetOf<Point>()

fun main() {
    val program = IntCode.fromFile("src/main/kotlin/day15/day15-input.txt")
    val todo = mutableListOf<Point>()

    todo.add(START)
    walkable.add(START)

    var target: Point?
    do {
        target = findTarget(program, todo)
    } while (target == null)

    println("Answer 1: " + target.route.size)

    val lastPoint = fillFrom(Point(target.x, target.y))
    println("Answer 2: " + lastPoint.route.size)
}

fun findTarget(program: IntCode, todo: MutableList<Point>): Point? {
    val input = Channel<Long>(UNLIMITED)
    val output = Channel<Long>(UNLIMITED)

    return runBlocking {
        val droid = launch {
            program.run(input, output)
        }
        val result = withContext(Dispatchers.Default) {
            explore(todo, input, output)
        }
        droid.cancel()
        result
    }
}

private suspend fun explore(todo: MutableList<Point>, input: Channel<Long>, output: Channel<Long>): Point? {
    try {
        while (todo.isNotEmpty()) {
            val point = todo.removeAt(0)
            walkTo(point, input, output)

            for (direction in Direction.values()) {
                val nextPoint = point.next(direction)

                if (!walls.contains(nextPoint) && !walkable.contains(nextPoint)) {
                    input.send(direction.id)

                    when (output.receive()) {
                        0L -> {
                            walls.add(nextPoint)
                        }
                        1L -> {
                            walkable.add(nextPoint)
                            todo.add(nextPoint)
                            stepBack(direction, input, output)
                        }
                        2L -> {
                            return nextPoint
                        }
                    }
                }
                //printGrid(point)
            }

            backtrack(point, input, output)
        }
    } catch (e: ClosedReceiveChannelException) {
        // ignore
    }

    return null
}

suspend fun walkTo(point: Point, input: Channel<Long>, output: Channel<Long>) {
    for (direction in point.route) {
        input.send(direction.id)
        output.receive()
    }
}

suspend fun backtrack(point: Point, input: Channel<Long>, output: Channel<Long>) {
    for (direction in point.route.reversed()) {
        stepBack(direction, input, output)
    }
}

suspend fun stepBack(direction: Direction, input: Channel<Long>, output: Channel<Long>) {
    input.send(direction.opposite().id)
    output.receive()
}

private fun printGrid(position: Point) {
    val walkableMin = walkable.min()
    val walkableMax = walkable.max()
    val wallsMin = walls.min()
    val wallsMax = walls.max()

    val minX = minOf(walkableMin.x, wallsMin.x)
    val minY = minOf(walkableMin.y, wallsMin.y)
    val maxX = maxOf(walkableMax.x, wallsMax.x)
    val maxY = maxOf(walkableMax.y, wallsMax.y)

    for (y in minY..maxY) {
        for (x in minX..maxX) {
            val point = Point(x, y)
            print(when {
                position == point -> 'O'
                walkable.contains(point) -> '.'
                walls.contains(point) -> '#'
                else -> ' '
            })
        }
        println()
    }
    println("-".repeat(80))
}

private fun Set<Point>.min() = if (isEmpty()) Point(Long.MAX_VALUE, Long.MAX_VALUE) else Point(map { it.x }.min()!!, map { it.y }.min()!!)
private fun Set<Point>.max() = if (isEmpty()) Point(Long.MIN_VALUE, Long.MIN_VALUE) else Point(map { it.x }.max()!!, map { it.y }.max()!!)

fun fillFrom(start: Point): Point {
    val todo = mutableListOf<Point>()
    todo.add(start)

    val checked = hashSetOf<Point>()
    var point = start

    while (todo.isNotEmpty()) {
        point = todo.removeAt(0)

        for (direction in Direction.values()) {
            val nextPoint = point.next(direction)

            if (!checked.contains(nextPoint) && !walls.contains(nextPoint)) {
                todo.add(nextPoint)
            }
        }

        checked.add(point)
    }

    return point
}
