package day07

import java.io.File


fun main() {
    val program: IntArray = File("src/main/kotlin/day07/day07-input.txt")
            .readLines()
            .first()
            .split(",")
            .map { it.toInt() }
            .toIntArray()

    var maxSignal = 0

    permutations(intArrayOf(0, 1, 2, 3, 4)) { phaseSettings ->
        val input1 = listOf(phaseSettings[0], 0).iterator()
        val output1 = run(program.copyOf(), input1)

        val input2 = listOf(phaseSettings[1], output1.next()).iterator()
        val output2 = run(program.copyOf(), input2)

        val input3 = listOf(phaseSettings[2], output2.next()).iterator()
        val output3 = run(program.copyOf(), input3)

        val input4 = listOf(phaseSettings[3], output3.next()).iterator()
        val output4 = run(program.copyOf(), input4)

        val input5 = listOf(phaseSettings[4], output4.next()).iterator()
        val output5 = run(program.copyOf(), input5)

        val signal = output5.next()

        if (signal > maxSignal) {
            maxSignal = signal
        }
    }

    println(maxSignal)
}

fun permutations(numbers: IntArray, run: (IntArray) -> Unit) = permutations(numbers, numbers.size, run)

private fun permutations(numbers: IntArray, n: Int, run: (IntArray) -> Unit) {
    if (n == 1) {
        run(numbers)
    } else {
        for (i in 0 until n) {
            numbers.swap(i, n - 1)
            permutations(numbers, n - 1, run)
            numbers.swap(i, n - 1)
        }
    }
}

private fun IntArray.swap(index1: Int, index2: Int) {
    val tmp = get(index1)
    set(index1, get(index2))
    set(index2, tmp)
}

private fun run(program: IntArray, input: Iterator<Int>): Iterator<Int> {
    var instructionPointer = 0
    var modes = listOf(0, 0, 0)
    var output = arrayListOf<Int>()

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
                program[program[instructionPointer + 1]] = input.next()
                instructionPointer += 2
            }
            4 -> {
                output.add(parameter(1))
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
                return output.iterator()
            }
            else -> {
                throw Error("unknown opcode: $opcode")
            }
        }
    }
}
