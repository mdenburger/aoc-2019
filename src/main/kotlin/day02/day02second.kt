package day02

import java.io.File
import kotlin.system.exitProcess

fun main() {
    val program: IntArray = File("src/main/kotlin/day02/day02-input.txt")
            .readLines()
            .first()
            .split(",")
            .map { it.toInt() }
            .toIntArray()

    for (noun in 1..100) {
        for (verb in 1..100) {
            try {
                val output = run(program.copyOf(), noun, verb)
                if (output == 19690720) {
                    println((100 * noun) + verb)
                    exitProcess(0)
                }
            } catch (e: ArrayIndexOutOfBoundsException) {
                // memory overflow, ignore
            }
        }
    }
}

fun run(program: IntArray, noun: Int, verb: Int): Int {
    program[1] = noun
    program[2] = verb

    var position = 0

    fun lookup(address: Int) = program[program[address]]

    while (true) {
        val opcode = program[position]
        when (opcode) {
            1 -> program[program[position + 3]] = lookup(position + 1) + lookup(position + 2)
            2 -> program[program[position + 3]] = lookup(position + 1) * lookup(position + 2)
            99 -> return program[0]
        }
        position += 4;
    }
}
