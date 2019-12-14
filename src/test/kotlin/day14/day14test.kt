package day14

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class day14test {

    var TRILLION = 1000000000000

    @Test
    fun minOrePerFuelExample1() {
        assertEquals(31, minimumOrePerFuel("src/main/kotlin/day14/example1.txt"))
    }

    @Test
    fun minOrePerFuelExample2() {
        assertEquals(165, minimumOrePerFuel("src/main/kotlin/day14/example2.txt"))
    }

    @Test
    fun minOrePerFuelExample3() {
        assertEquals(13312, minimumOrePerFuel("src/main/kotlin/day14/example3.txt"))
    }

    @Test
    fun minOrePerFuelExample4() {
        assertEquals(180697, minimumOrePerFuel("src/main/kotlin/day14/example4.txt"))
    }

    @Test
    fun minOrePerFuelExample5() {
        assertEquals(2210736, minimumOrePerFuel("src/main/kotlin/day14/example5.txt"))
    }

    @Test
    fun maxFuelForOreExample3() {
        assertEquals(82892753, maximumFuelForOre("src/main/kotlin/day14/example3.txt", TRILLION))
    }

    @Test
    fun maxFuelForOreExample4() {
        assertEquals(5586022, maximumFuelForOre("src/main/kotlin/day14/example4.txt", TRILLION))
    }

    @Test
    fun maxFuelForOreExample5() {
        assertEquals(460664, maximumFuelForOre("src/main/kotlin/day14/example5.txt", TRILLION))
    }
}
