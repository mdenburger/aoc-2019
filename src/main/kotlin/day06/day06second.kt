package day06

import java.io.File

private val orbited = hashMapOf<String, String?>()

fun <T> MutableList<T>.removeLast() = removeAt(size - 1)

fun main() {
    orbited["COM"] = null

    File("src/main/kotlin/day06/day06-input.txt").forEachLine {
        val data = it.split(")")
        val orbitedThing = data[0]
        val thing = data[1]
        orbited[thing] = orbitedThing
    }

    val youPath = pathToCom("YOU")
    val santaPath = pathToCom("SAN")

    while (youPath.last() == santaPath.last()) {
        youPath.removeLast()
        santaPath.removeLast()
    }

    println(youPath.size + santaPath.size - 2)
}

private fun pathToCom(startThing: String?): MutableList<String?> {
    val path = mutableListOf<String?>()
    var thing = startThing

    do {
        path.add(thing)
        thing = orbited[thing]
    } while (thing != null)

    return path
}