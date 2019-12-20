package day19

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import shared.IntCode
import kotlin.math.max

fun main() {
    part1()
    part2()
}

fun part1() {
    val program = IntCode.fromFile("src/main/kotlin/day19/day19-input.txt")
    val answer1 = scanGrid(program)
    println("Answer 1: $answer1")
}

private fun scanGrid(program: IntCode): Int {
    var pulledCount = 0

    for (y in 0..49) {
        for (x in 0..49) {
            val state = scanPoint(program.copyOf(), x, y)
            when(state) {
                0 -> print(".")
                else -> {
                    print('#')
                    pulledCount++
                }
            }
        }
        println()
    }

    return pulledCount
}

fun part2() {
    val program = IntCode.fromFile("src/main/kotlin/day19/day19-input.txt")
    val answer2 = fitSquareInBeam(program, 100)
    println("Answer 2: $answer2")
}

private fun fitSquareInBeam(program: IntCode, squareSize: Int): Int {
    val startX = hashMapOf<Int, Int>()
    val endX = hashMapOf<Int, Int>()
    var x = 0
    var y = 3
    var inBeam = false
    var done = false

    startX[y] = x
    startX[y + 1] = x

    while(!done) {
        when (scanPoint(program.copyOf(), x, y)) {
            0 -> {
                if (inBeam) {
                    // left the beam on this line
                    inBeam = false
                    endX[y] = x - 1

                    if (y >= squareSize && (endX[y - squareSize + 1] ?: 0) - (startX[y] ?: 0) + 1 >= squareSize) {
                        done = true
                    } else {
                        // go to next line
                        x = startX[y] ?: 0
                        y += 1
                    }
                } else {
                    // continue to start of beam
                    x += 1
                }
            }
            else -> {
                // continue to end of beam in line...
                if (!inBeam) {
                    // beam starts here
                    inBeam = true
                    startX[y] = x
                    startX[y + 1] = x
                    x = max(endX[y - 1] ?: 0, x + 1)
                } else {
                    x += 1
                }
            }
        }
    }

    return (startX[y]!! * 10000) + (y - squareSize + 1)
}

private fun scanPoint(program: IntCode, x: Int, y: Int): Int {
    val input = Channel<Long>(Channel.UNLIMITED)
    val output = Channel<Long>(Channel.UNLIMITED)

    return runBlocking {
        val process = launch {
            program.run(input, output)
        }
        val result = withContext(Dispatchers.Default) {
            input.send(x.toLong())
            input.send(y.toLong())
            val result = output.receive().toInt()
            input.close()
            output.close()
            result
        }
        process.cancel()
        result
    }
}
