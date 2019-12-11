package day1

import java.io.File

fun main() {
    var result = 0;
    File("src/main/kotlin/day01/day01-input.txt").forEachLine {
        val weight = it.toInt()
        val fuel = (weight / 3) - 2
        result += fuel;
    }
    println(result)
}