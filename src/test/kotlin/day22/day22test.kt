package day22

import shuffleDeck
import cut
import dealIntoNewStack
import dealWithIncrement
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class day22test {

    @Test
    fun testDealIntoNewStack() {
        assertEquals(intArrayOf(9, 8, 7, 6, 5, 4, 3, 2, 1, 0).toList(),
                intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).dealIntoNewStack().toList())
    }

    @Test
    fun cutPositive() {
        assertEquals(intArrayOf(3, 4, 5, 6, 7, 8, 9, 0, 1, 2).toList(),
                intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).cut(3).toList())
    }

    @Test
    fun cutNegative() {
        assertEquals(intArrayOf(6, 7, 8, 9, 0, 1, 2, 3, 4, 5).toList(),
                intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).cut(-4).toList())
    }

    @Test
    fun dealWithIncrement3() {
        assertEquals(intArrayOf(0, 7, 4, 1, 8, 5, 2, 9, 6, 3).toList(),
                intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).dealWithIncrement(3).toList())
    }

    @Test
    fun example1() {
        assertEquals(intArrayOf(0, 3, 6, 9, 2, 5, 8, 1, 4, 7).toList(),
                shuffleDeck("src/main/kotlin/day22/example1.txt", 10).toList())
    }

    @Test
    fun example2() {
        assertEquals(intArrayOf(3, 0, 7, 4, 1, 8, 5, 2, 9, 6).toList(),
                shuffleDeck("src/main/kotlin/day22/example2.txt", 10).toList())
    }

    @Test
    fun example3() {
        assertEquals(intArrayOf(6, 3, 0, 7, 4, 1, 8, 5, 2, 9).toList(),
                shuffleDeck("src/main/kotlin/day22/example3.txt", 10).toList())
    }
    @Test
    fun example4() {
        assertEquals(intArrayOf(9, 2, 5, 8, 1, 4, 7, 0 ,3 ,6).toList(),
                shuffleDeck("src/main/kotlin/day22/example4.txt", 10).toList())
    }
}

