package day08

import java.io.File

fun main() {
    val program: IntArray = File("src/main/kotlin/day08/day08-input.txt")
            .readLines()
            .first()
            .split("")
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
            .toIntArray()

    var minZeros = Int.MAX_VALUE
    var minLayer = emptyList<Int>()

    for (start in program.indices step 25 * 6) {
        val layer = program.slice(IntRange(start, start + (25 * 6) - 1))
        val zeroCount = layer.filter { it == 0 }.count()
        if (zeroCount < minZeros) {
            minZeros = zeroCount
            minLayer = layer
        }
    }

    val oneCount = minLayer.filter { it == 1 }.count()
    val twoCount = minLayer.filter { it == 2 }.count()

    println(oneCount * twoCount)
}