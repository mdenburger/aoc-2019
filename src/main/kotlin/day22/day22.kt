import java.io.File
import kotlin.math.absoluteValue

fun main() {
    part1()
}

fun part1() {
    val deck = shuffleDeck("src/main/kotlin/day22/day22-input.txt", 10007)
    println("Answer 1: ${deck.indexOf(2019)}")
}

fun shuffleDeck(fileName: String, deckSize: Int): IntArray {
    var deck = factoryOrder(deckSize)
    File(fileName).forEachLine {
        when {
            DEAL_INTO_NEW_STACK matches it -> {
                deck = deck.dealIntoNewStack()
            }
            CUT_N matches it -> {
                val n = CUT_N.find(it)?.groupValues?.get(1)?.toInt() ?: throw Error("Unknown N in line '$it'")
                deck = deck.cut(n)
            }
            DEAL_WITH_INCREMENT_N matches it -> {
                val n = DEAL_WITH_INCREMENT_N.find(it)?.groupValues?.get(1)?.toInt() ?: throw Error("Unknown increment in line '$it'")
                deck = deck.dealWithIncrement(n)
            }
        }
    }
    return deck
}

fun factoryOrder(size: Int): IntArray {
    return IntArray(size) { it }
}

val DEAL_INTO_NEW_STACK = """^deal into new stack$""".toRegex()
val CUT_N = """^cut (-?\d+)$""".toRegex()
val DEAL_WITH_INCREMENT_N = """^deal with increment (\d+)$""".toRegex()

fun IntArray.dealIntoNewStack() = copyOf().apply { reverse() }

fun IntArray.cut(n: Int): IntArray {
    val result = IntArray(size)
    if (n >= 0) {
        copyInto(result, size - n, 0, n)
        copyInto(result, 0, n, size)
    } else {
        copyInto(result, 0, size + n, size)
        copyInto(result, n.absoluteValue, 0, size + n)
    }
    return result
}

fun IntArray.dealWithIncrement(n: Int): IntArray {
    val result = IntArray(size)
    var position = 0
    for (index in 0..lastIndex) {
        result[position] = get(index)
        position = (position + n) % size
    }
    return result
}
