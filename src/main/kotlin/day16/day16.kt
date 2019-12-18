package day16

import java.io.File
import kotlin.math.absoluteValue

val BASE_PATTERN = intArrayOf(0, 1, 0, -1)

fun main() {
    part1()
    part2()
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

fun part2() {
    val numbers = readNumbers(File("src/main/kotlin/day16/day16-input.txt").readText())
    val answer2 = calculateMessage(numbers)
    println("Answer 2: $answer2")
}

fun calculateMessage(numbers: IntArray): String {
    val times = 10000
    val messageOffset = numbers.copyOfRange(0, 7).joinToString("").toInt()

    if (messageOffset < numbers.size * times / 2) {
        throw NotImplementedError("Can't do message offsets in the first half")
    }

    val end = numbers.repeat(times).subList(messageOffset, numbers.size * times).toIntArray()
    val result = transformFast(end, 100)

    return result.copyOfRange(0, 8).joinToString("")
}

fun IntArray.repeat(times: Int): List<Int> = sequence {
    repeat(times) {
        yieldAll(asSequence())
    }
}.toList()

fun transformFast(numbers: IntArray, times: Int): IntArray {
    var current = numbers;

    repeat(times) {
        val next = IntArray(current.size)

        next[next.lastIndex] = current.last()

        for (index in current.lastIndex - 1 downTo 0) {
            next[index] = (current[index] + next[index + 1]) % 10
        }

        current = next
    }

    return current
}
