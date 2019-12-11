package day1

import java.io.File

fun main() {
    var result = 0;
    File("src/main/kotlin/day01/day01-input.txt").forEachLine {
        val weight = it.toInt()
        result += fuel(weight);
    }
    println(result)
}

fun fuel(mass: Int): Int {
    var fuel = mass
    var result = 0;

    do {
        fuel = (fuel / 3) - 2;
        result += fuel;
    } while (fuel > 8)

    return result;
}
