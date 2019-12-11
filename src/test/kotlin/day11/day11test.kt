package day11

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.lang.Math.PI

class day11test {

    @Test
    fun direction() {
        assertEquals(Direction.LEFT, Direction.UP.change(Turn.LEFT))
        assertEquals(Direction.RIGHT, Direction.UP.change(Turn.RIGHT))
        assertEquals(Direction.DOWN, Direction.LEFT.change(Turn.LEFT))
        assertEquals(Direction.UP, Direction.LEFT.change(Turn.RIGHT))
    }
}
