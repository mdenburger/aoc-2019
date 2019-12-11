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

    program[1] = 12;
    program[2] = 2;

    var position = 0

    fun lookup(address: Int) = program[program[address]]

    while (true) {
        val opcode = program[position]
        when (opcode) {
            1 -> program[program[position + 3]] = lookup(position + 1) + lookup(position + 2)
            2 -> program[program[position + 3]] = lookup(position + 1) * lookup(position + 2)
            99 -> {
                println(program[0])
                exitProcess(0);
            }
        }
        position += 4;
    }
}
