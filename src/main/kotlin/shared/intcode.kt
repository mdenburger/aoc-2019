package shared

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import java.io.File

class IntCode constructor(fileName: String) {
    private val code = hashMapOf<Long, Long>()
    private var instructionPointer = 0L
    private var relativeBase = 0L
    private val modes = hashMapOf<Int, Int>()
    private var done = false

    init {
        File(fileName)
                .readText()
                .trim()
                .split(",")
                .mapIndexed { index, number -> code[index.toLong()] = number.toLong() }
    }

    operator fun set(address: Long, value: Long) {
        code[address] = value
    }

    suspend fun run(input: ReceiveChannel<Long>, output: SendChannel<Long>) {
        while (!done) {
            val instruction = code[instructionPointer].toString()

            val opcode = instruction.takeLast(2).toInt()

            modes.clear()
            instruction.take(Integer.max(instruction.length - 2, 0))
                    .reversed()
                    .mapIndexed { index, value -> modes.put(index, value.toString().toInt()) }

            when (opcode) {
                1 -> {
                    store(3, parameter(1) + parameter(2))
                    instructionPointer += 4
                }
                2 -> {
                    store(3, parameter(1) * parameter(2))
                    instructionPointer += 4
                }
                3 -> {
                    store(1, input.receive())
                    instructionPointer += 2
                }
                4 -> {
                    output.send(parameter(1))
                    instructionPointer += 2
                }
                5 -> {
                    if (parameter(1) != 0L) {
                        instructionPointer = parameter(2)
                    } else {
                        instructionPointer += 3
                    }
                }
                6 -> {
                    if (parameter(1) == 0L) {
                        instructionPointer = parameter(2)
                    } else {
                        instructionPointer += 3
                    }
                }
                7 -> {
                    if (parameter(1) < parameter(2)) {
                        store(3, 1)
                    } else {
                        store(3, 0)
                    }
                    instructionPointer += 4
                }
                8 -> {
                    if (parameter(1) == parameter(2)) {
                        store(3, 1)
                    } else {
                        store(3, 0)
                    }
                    instructionPointer += 4
                }
                9 -> {
                    relativeBase += parameter(1)
                    instructionPointer += 2
                }
                99 -> {
                    output.close()
                    done = true
                }
                else -> {
                    throw Error("unknown opcode: $opcode")
                }
            }
        }
    }

    private fun store(parameter: Int, value: Long) = when (mode(parameter)) {
        0 -> code[code[instructionPointer + parameter] ?: 0L] = value
        2 -> code[relativeBase + (code[instructionPointer + parameter] ?: 0L)] = value
        else -> throw Error("unknown mode for storage of param $parameter: $value")
    }

    private fun parameter(number: Int) = lookup(instructionPointer + number, mode(number))

    private fun lookup(address: Long, mode: Int): Long {
        return when (mode) {
            0 -> code[code[address] ?: 0] ?: 0
            1 -> code[address] ?: 0
            2 -> code[relativeBase + (code[address] ?: 0)] ?: 0
            else -> throw Error("unknown mode for address $address: $mode")
        }
    }

    private fun mode(number: Int) = modes.getOrDefault(number - 1, 0)
}
