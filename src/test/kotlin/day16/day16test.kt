package day16

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class day16test {

    @Test
    fun onesDigit() {
        assertEquals(1, 1.onesDigit())
        assertEquals(1, (-1).onesDigit())
        assertEquals(0, 10.onesDigit())
        assertEquals(0, (-10).onesDigit())
        assertEquals(3, 123.onesDigit())
        assertEquals(3, (-123).onesDigit())
    }

    @Test
    fun fftSequenceZero() {
        val sequence = fftSequence(0)
        assertEquals(1, sequence.next())
        assertEquals(0, sequence.next())
        assertEquals(-1, sequence.next())
        assertEquals(0, sequence.next())
        assertEquals(1, sequence.next())
        assertEquals(0, sequence.next())
        assertEquals(-1, sequence.next())
    }

    @Test
    fun fftSequenceOne() {
        val sequence = fftSequence(1)
        assertEquals(0, sequence.next())
        assertEquals(1, sequence.next())
        assertEquals(1, sequence.next())
        assertEquals(0, sequence.next())
        assertEquals(0, sequence.next())
        assertEquals(-1, sequence.next())
        assertEquals(-1, sequence.next())
        assertEquals(0, sequence.next())
        assertEquals(0, sequence.next())
        assertEquals(1, sequence.next())
        assertEquals(1, sequence.next())
        assertEquals(0, sequence.next())
        assertEquals(0, sequence.next())
        assertEquals(-1, sequence.next())
        assertEquals(-1, sequence.next())
    }

    @Test
    fun fftSum() {
        assertEquals(4, intArrayOf(1, 2, 3, 4, 5, 6, 7, 8).fftSum(0).onesDigit())
        assertEquals(8, intArrayOf(1, 2, 3, 4, 5, 6, 7, 8).fftSum(1).onesDigit())
        assertEquals(2, intArrayOf(1, 2, 3, 4, 5, 6, 7, 8).fftSum(2).onesDigit())
        assertEquals(2, intArrayOf(1, 2, 3, 4, 5, 6, 7, 8).fftSum(3).onesDigit())
        assertEquals(6, intArrayOf(1, 2, 3, 4, 5, 6, 7, 8).fftSum(4).onesDigit())
        assertEquals(1, intArrayOf(1, 2, 3, 4, 5, 6, 7, 8).fftSum(5).onesDigit())
        assertEquals(5, intArrayOf(1, 2, 3, 4, 5, 6, 7, 8).fftSum(6).onesDigit())
        assertEquals(8, intArrayOf(1, 2, 3, 4, 5, 6, 7, 8).fftSum(7).onesDigit())
    }

    @Test
    fun transformFast() {
        val example = intArrayOf(5, 6, 7, 8)

        val phase1 = transformFast(example, 1)
        assertEquals(listOf(6, 1, 5, 8), phase1.toList())

        val phase2 = transformFast(phase1, 1)
        assertEquals(listOf(0, 4, 3, 8), phase2.toList())

        val phase3 = transformFast(phase2, 1)
        assertEquals(listOf(5, 5, 1, 8), phase3.toList())
    }

    @Test
    fun calculateMessage() {
        assertEquals("84462026", calculateMessage(readNumbers("03036732577212944063491565474664")))
        assertEquals("78725270", calculateMessage(readNumbers("02935109699940807407585447034323")))
        assertEquals("53553731", calculateMessage(readNumbers("03081770884921959731165446850517")))
    }
}