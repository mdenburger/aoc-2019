package day06

import java.io.File

private val orbited = hashMapOf<String, String?>()

fun main() {
    orbited["COM"] = null

    File("src/main/kotlin/day06/day06-input.txt").forEachLine {
        val data = it.split(")")
        val orbitedThing = data[0]
        val thing = data[1]
        orbited[thing] = orbitedThing
    }

    println(orbited.keys.map(::stepsToCom).sum())
}

private fun stepsToCom(thing: String?): Int = if (thing == "COM") 0 else 1 + stepsToCom(orbited[thing])
