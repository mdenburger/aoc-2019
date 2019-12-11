package day07

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

fun main() {
    val program: IntArray = File("src/main/kotlin/day07/day07-input.txt")
            .readLines()
            .first()
            .split(",")
            .map { it.toInt() }
            .toIntArray()

    var maxSignal = 0

    permutations(intArrayOf(5, 6, 7, 8, 9)) { phaseSettings ->
        val channels = arrayListOf<Channel<Int>>()

        repeat(5) {
            channels.add(Channel())
        }

        var signal = 0

        runBlocking {
            for (i in 0..4) {
                launch {
                    signal += run(i, program.copyOf(), channels[i], channels[(i + 1) % 5])
                }
                channels[i].send(phaseSettings[i])
            }
            channels[0].send(0)
        }

        if (signal > maxSignal) {
            maxSignal = signal
        }
    }

    println(maxSignal)
}

private suspend fun run(id: Int, program: IntArray, input: ReceiveChannel<Int>, output: SendChannel<Int>): Int {
    var instructionPointer = 0
    var modes = listOf(0, 0, 0)

    fun lookup(address: Int, mode: Int) = when (mode) {
        0 -> program[program[address]]
        1 -> program[address]
        else -> throw Error("unknown mode for address $address: $mode")
    }

    fun parameter(number: Int) = lookup(instructionPointer + number, modes[number - 1])

    while (true) {
        val instruction = "000" + program[instructionPointer].toString()

        val opcode = instruction.substring(instruction.length - 2).toInt()
        modes = instruction.substring(0, instruction.length - 2).toCharArray().map { it.toString().toInt() }.reversed()

        when (opcode) {
            1 -> {
                program[program[instructionPointer + 3]] = parameter(1) + parameter(2)
                instructionPointer += 4
            }
            2 -> {
                program[program[instructionPointer + 3]] = parameter(1) * parameter(2)
                instructionPointer += 4
            }
            3 -> {
                program[program[instructionPointer + 1]] = input.receive()
                instructionPointer += 2
            }
            4 -> {
                output.send(parameter(1))
                instructionPointer += 2
            }
            5 -> {
                if (parameter(1) != 0) {
                    instructionPointer = parameter(2)
                } else {
                    instructionPointer += 3
                }
            }
            6 -> {
                if (parameter(1) == 0) {
                    instructionPointer = parameter(2)
                } else {
                    instructionPointer += 3
                }
            }
            7 -> {
                if (parameter(1) < parameter(2)) {
                    program[program[instructionPointer + 3]] = 1
                } else {
                    program[program[instructionPointer + 3]] = 0
                }
                instructionPointer += 4
            }
            8 -> {
                if (parameter(1) == parameter(2)) {
                    program[program[instructionPointer + 3]] = 1
                } else {
                    program[program[instructionPointer + 3]] = 0
                }
                instructionPointer += 4
            }
            99 -> {
                return if (id == 0) input.receive() else 0
            }
            else -> {
                throw Error("unknown opcode: $opcode")
            }
        }
    }
}
