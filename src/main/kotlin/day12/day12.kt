package day12

import java.io.File
import java.util.Arrays
import kotlin.math.absoluteValue


data class Moon(var position: Tuple, var velocity: Tuple = Tuple(0, 0, 0)) {
    fun totalEnergy() = potentialEnergy() * kineticEnergy()
    private fun potentialEnergy() = position.absoluteSum()
    private fun kineticEnergy() = velocity.absoluteSum()
}

data class Tuple(val x: Int, val y: Int, val z: Int) {
    operator fun plus(other: Tuple) = Tuple(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Tuple) = Tuple(x - other.x, y - other.y, z - other.z)

    private fun coerceIn(range: ClosedRange<Int>) = Tuple(x.coerceIn(range), y.coerceIn(range), z.coerceIn(range))

    fun getPullTo(other: Tuple) = (other - this).coerceIn(-1..1)

    fun absoluteSum() = x.absoluteValue + y.absoluteValue + z.absoluteValue
}

fun main() {
    val moons = readMoons("src/main/kotlin/day12/day12-input.txt")
    part1(moons.toList())
    part2(moons.toList())
}

fun part1(moons: List<Moon>) {
    simulateMotion(moons, 1000)
    println("Answer 1: " + totalEnergy(moons))
}

fun part2(moons: List<Moon>) {
    val statesX = hashSetOf<List<Int>>()
    val statesY = hashSetOf<List<Int>>()
    val statesZ = hashSetOf<List<Int>>()

    do {
        simulateMotion(moons, 1)

        val stateX = moons.map { it.position.x }.toMutableList() + moons.map { it.velocity.x }.toMutableList()
        val stateY = moons.map { it.position.y }.toMutableList() + moons.map { it.velocity.y }.toMutableList()
        val stateZ = moons.map { it.position.z }.toMutableList() + moons.map { it.velocity.z }.toMutableList()

        val newX = statesX.add(stateX)
        val newY = statesY.add(stateY)
        val newZ = statesZ.add(stateZ)

    } while (newX || newY || newZ)

    println("Answer 2: " + lcm(statesX.size.toLong(), statesY.size.toLong(), statesZ.size.toLong()))
}

fun readMoons(fileName: String): List<Moon> {
    val moons = arrayListOf<Moon>()
    val number = "-?\\d+".toRegex()

    File(fileName).forEachLine { line ->
        val (x, y, z) = number.findAll(line).map { it.value.toInt() }.toList()
        moons.add(Moon(Tuple(x, y, z)))
    }

    return moons
}

fun simulateMotion(moons: List<Moon>, rounds: Int) {
    repeat(rounds) {
        applyGravity(moons)
        applyVelocity(moons)
    }
}

private fun applyGravity(moons: List<Moon>) {
    for (i in 0 until moons.size - 1) {
        val moon1 = moons[i]

        for (j in i + 1 until moons.size) {
            val moon2 = moons[j]

            moon1.velocity += moon1.position.getPullTo(moon2.position)
            moon2.velocity += moon2.position.getPullTo(moon1.position)
        }
    }
}

fun applyVelocity(moons: List<Moon>) = moons.forEach { it.position += it.velocity }

fun totalEnergy(moons: List<Moon>) = moons.map { it.totalEnergy() }.sum()

fun lcm(vararg numbers: Long): Long = Arrays.stream(numbers).reduce(1) { x: Long, y: Long -> x * (y / gcd(x, y)) }

fun gcd(x: Long, y: Long): Long = if (y == 0L) x else gcd(y, x % y)
