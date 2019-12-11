package day08

import java.io.File

val WIDTH = 25
val HEIGHT = 6

fun main() {
    val program: IntArray = File("src/main/kotlin/day08/day08-input.txt")
            .readLines()
            .first()
            .split("")
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
            .toIntArray()

    val layers = arrayListOf<List<Int>>()
    val size = WIDTH * HEIGHT

    for (start in program.indices step size) {
        val layer = program.slice(IntRange(start, start + size - 1))
        layers.add(layer)
    }

    val image = IntArray(size)
    image.fill(2)

    for (layer in layers) {
        for (i in 0 until size) {
            if (image[i] == 2 && layer[i] != 2) {
                image[i] = layer[i]
            }
        }
    }

    for (row in image.indices step WIDTH) {
        println(image
                .slice(IntRange(row, row + WIDTH - 1))
                .map { if (it == 1) "\u2588\u2588" else "  " }
                .joinToString("")
        )
    }
}