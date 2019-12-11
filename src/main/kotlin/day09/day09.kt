package day09

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import shared.IntCode

fun main() {
    println("Answer 1: " + run(1L))
    println("Answer 2: " + run(2L))
}

private fun run(inputValue: Long): Long {
    val program = IntCode("src/main/kotlin/day09/day09-input.txt")

    return runBlocking {
        val input = Channel<Long>(1)
        val output = Channel<Long>(1)

        launch {
            program.run(input, output)
        }
        run {
            input.send(inputValue)
            output.receive()
        }
    }
}
