package day04

fun main() {
    val start = 156218
    val end = 652527
    var solutions = 0

    test(111111, false)
    test(223450, false)
    test(123789, false)
    test(112233, true)
    test(123444, false)
    test(111122, true)

    for (number in start..end) {
        if (isValidPassword(number)) {
            solutions++
        }
    }

    println(solutions);
}

fun test(number: Int, ok: Boolean) {
    val result = isValidPassword(number)
    if (result != ok) {
        throw Error("Failed: $number ($result instead of $ok)")
    }
}

private fun isValidPassword(number: Int): Boolean {
    val password = number.toString().map(Character::getNumericValue)
    val count = IntArray(10)

    count[password[0]]++

    for (index in 1..5) {
        val prev = password[index - 1]
        val current = password[index]
        if (prev > current) {
            return false
        }
        count[current]++
    }

    return count.contains(2)
}
