package day05

import java.io.File

fun main() {
    val program: IntArray = File("src/main/kotlin/day05/day05-input.txt")
            .readLines()
            .first()
            .split(",")
            .map { it.toInt() }
            .toIntArray()

    run(program)
}

private fun run(program: IntArray) {
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
                // hardcode input "5"
                program[program[instructionPointer + 1]] = 5
                instructionPointer += 2
            }
            4 -> {
                println(parameter(1))
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
                return
            }
            else -> {
                throw Error("unknown opcode: $opcode")
            }
        }
    }
}
