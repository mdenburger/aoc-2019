package day12

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class day12test {

    @Test
    fun getPullTo() {
        assertEquals(Tuple(1, 0, -1), Tuple(3, 4, 5).getPullTo(Tuple(5, 4, 3)))
    }

    @Test
    fun applyVelocity() {
        val moon = Moon(Tuple(1, 2, 3), Tuple(-2, 0, 3))
        moon.position += moon.velocity
        assertEquals(Tuple(-1, 2, 6), moon.position)
    }

    @Test
    fun max() {
        println(Int.MAX_VALUE)
    }
}
