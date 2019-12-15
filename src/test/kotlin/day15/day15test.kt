package day15

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class day15test {

    @Test
    fun opposites() {
        assertEquals(Direction.SOUTH, Direction.NORTH.opposite())
        assertEquals(Direction.WEST, Direction.EAST.opposite())
        assertEquals(Direction.NORTH, Direction.SOUTH.opposite())
        assertEquals(Direction.EAST, Direction.WEST.opposite())

        assertNotEquals(Direction.EAST, Direction.NORTH.opposite())
        assertNotEquals(Direction.WEST, Direction.NORTH.opposite())
        assertNotEquals(Direction.NORTH, Direction.EAST.opposite())
        assertNotEquals(Direction.SOUTH, Direction.EAST.opposite())
        assertNotEquals(Direction.EAST, Direction.SOUTH.opposite())
        assertNotEquals(Direction.WEST, Direction.SOUTH.opposite())
        assertNotEquals(Direction.NORTH, Direction.WEST.opposite())
        assertNotEquals(Direction.SOUTH, Direction.WEST.opposite())
    }
}
