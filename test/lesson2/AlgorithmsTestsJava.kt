package lesson2

import lesson2.JavaAlgorithms.longestCommonSubstring
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.assertThrows
import java.lang.Exception
import kotlin.test.Test
import kotlin.test.assertEquals

class AlgorithmsTestsJava : AbstractAlgorithmsTests() {
    @Test
    @Tag("Easy")
    fun testOptimizeBuyAndSell() {
        optimizeBuyAndSell { JavaAlgorithms.optimizeBuyAndSell(it) }
    }

    @Test
    @Tag("Easy")
    fun testJosephTask() {
        josephTask { menNumber, choiceInterval -> JavaAlgorithms.josephTask(menNumber, choiceInterval) }
    }

    @Test
    @Tag("Normal")
    fun testLongestCommonSubstring() {
        assertEquals("", longestCommonSubstring("", "")) //empty input case
        assertEquals("1234", longestCommonSubstring("123456", "78901234")) //some simple case testing
        assertEquals("ong", longestCommonSubstring("Prolong", "Longest")) //case sensitivity testing
        assertEquals("abcde", longestCommonSubstring("abcde", "abcde")) //similar strings testing
    }

    @Test
    @Tag("Easy")
    fun testCalcPrimesNumber() {
        calcPrimesNumber { JavaAlgorithms.calcPrimesNumber(it) }
        assertEquals(2, JavaAlgorithms.calcPrimesNumber(3))
        assertEquals(3, JavaAlgorithms.calcPrimesNumber(5))
        assertEquals(4, JavaAlgorithms.calcPrimesNumber(7))
    }

    @Test
    @Tag("Hard")
    fun testBaldaSearcher() {
        baldaSearcher { inputName, words -> JavaAlgorithms.baldaSearcher(inputName, words) }
        assertThrows<Exception> { JavaAlgorithms.baldaSearcher("input/balda_in4.txt", setOf("ABC")) }

    }
}