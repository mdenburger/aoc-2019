package day19

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import shared.IntCode

fun main() {
    part1()
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
