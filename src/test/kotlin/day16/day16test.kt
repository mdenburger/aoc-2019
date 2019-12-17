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
}