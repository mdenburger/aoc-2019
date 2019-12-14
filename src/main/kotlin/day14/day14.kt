package day14

import java.io.File
import kotlin.math.ceil

data class Chemical(val name: String, val amount: Int) {
    companion object Factory {
        fun fromString(s: String): Chemical {
            val valueName = s.split(" ")
            return Chemical(valueName[1], valueName[0].toInt())
        }
    }
}

data class Reaction(val inputs: List<Chemical>, val output: Chemical)

fun main() {
    println("Answer 1: " + minimumOrePerFuel("src/main/kotlin/day14/day14-input.txt"))
    println("Answer 2: " + maximumFuelForOre("src/main/kotlin/day14/day14-input.txt", 1000000000000))
}

fun minimumOrePerFuel(fileName: String): Long {
    return minimumOrePerFuel(readSources(fileName))
}

fun minimumOrePerFuel(sources: Map<Chemical, List<Chemical>>, leftOvers: MutableMap<String, Long> = hashMapOf()): Long {
    val needed = linkedMapOf<String, Long>()
    needed["FUEL"] = 1

    var ore = 0L

    while (needed.isNotEmpty()) {
        val neededMaterial = needed.keys.first()
        val neededAmount = needed.remove(neededMaterial)!!

        val output = sources.keys.find { it.name == neededMaterial } ?: throw Error("Don't know how to make $neededMaterial")
        val inputs = sources[output]

        val produceAmount = ceil(neededAmount / output.amount.toFloat()).toInt()

        inputs!!.forEach {
            val neededInput = it.amount * produceAmount
            val existing = leftOvers.remove(it.name) ?: 0
            if (existing >= neededInput) {
                leftOvers[it.name] = existing - neededInput
            } else if (it.name == "ORE") {
                ore += neededInput
            } else {
                val alreadyNeeded = needed[it.name] ?: 0
                needed[it.name] = alreadyNeeded + neededInput - existing
            }
        }

        val remainingOutput = (produceAmount * output.amount) - neededAmount
        if (remainingOutput > 0) {
            leftOvers[output.name] = remainingOutput
        }
    }

    return ore
}

fun maximumFuelForOre(fileName: String, maxOre: Long): Long {
    val sources = readSources(fileName)
    var fuel = 0L
    var ore = 0L
    val leftOvers = hashMapOf<String, Long>()

    while (ore < maxOre) {
        ore += minimumOrePerFuel(sources, leftOvers)
        fuel++
    }

    return if (ore == maxOre) fuel else fuel - 1
}

fun readSources(fileName: String) = readReactions(fileName).map { it.output to it.inputs }.toMap()

fun readReactions(fileName: String): List<Reaction> =
    File(fileName).readLines().map { line ->
        val reaction = line.split("=>").map { it.trim() }

        val left = reaction[0].split(',').map { it.trim() }
        val right = reaction[1]

        val input = left.map { Chemical.fromString(it) }
        val output = Chemical.fromString(right)

        Reaction(input, output)
    }

