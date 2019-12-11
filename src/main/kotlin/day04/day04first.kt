package day04

fun main() {
    val start = 156218
    val end = 652527
    var solutions = 0

    for (number in start..end) {
        if (isValidPassword(number)) {
            solutions++
        }
    }

    println(solutions);
}

private fun isValidPassword(number: Int): Boolean {
    val password = number.toString()
    var pairFound = false

    for (index in 1..5) {
        val prev = password[index - 1]
        val current = password[index]
        if (prev > current) {
            return false
        }
        if (prev == current) {
            pairFound = true
        }
    }

    return pairFound
}
