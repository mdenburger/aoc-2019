package day16

import java.io.File
import kotlin.math.absoluteValue

val BASE_PATTERN = intArrayOf(0, 1, 0, -1)

fun main() {
    part1()
}

fun part1() {
    val numbers = readNumbers(File("src/main/kotlin/day16/day16-input.txt").readText())
    val result = transform(numbers, 100)
    val answer1 = result.copyOfRange(0, 8).joinToString("")
    println("Answer 1: $answer1")
}

fun readNumbers(input: String) = input.split("")
        .filter { it.isNotEmpty() }
        .map { it.toInt() }
        .toIntArray()

fun transform(numbers: IntArray, times: Int): IntArray {
    var result = numbers;

    repeat(times) {
        result = result.mapIndexed { index, _ -> result.fftSum(index).onesDigit() }.toIntArray()
    }

    return result
}

fun IntArray.fftSum(index: Int): Int {
    val sequence = fftSequence(index)
    return this.map { it * sequence.next() }.sum()
}

fun fftSequence(index: Int) = sequence {
    val times = index + 1
    var first = true
    while(true) {
        BASE_PATTERN.forEachIndexed { index, value ->
            repeat(if (first && index == 0) times - 1 else times) {
                yield(value)
            }
        }
        first = false
    }
}.iterator()

fun Int.onesDigit() = this.absoluteValue % 10
