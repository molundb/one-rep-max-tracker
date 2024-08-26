package net.martinlundberg.onerepmaxtracker.util

import junit.framework.TestCase.assertEquals
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitRepository.Companion.multiplyIfPoundsAndRoundToNearestQuarter
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitRepository.WeightUnit.KILOGRAMS
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitRepository.WeightUnit.POUNDS
import org.junit.Test

internal class WeightUnitRepositoryTest {

    @Test
    fun givenWeightUnitIsKilograms_testMultiplyIfPoundsAndRoundToNearestQuarter() {
        val givenToExpected = mutableMapOf(
            100f to 100,
            46.11339603f to 46,
            25.245251f to 25.25f,
            100.42363262f to 100.5f,
            95.6735f to 95.75f,
            43.9353f to 44,
        )
        val weightUnit = KILOGRAMS

        for (testCase in givenToExpected) {
            val res = testCase.key.multiplyIfPoundsAndRoundToNearestQuarter(weightUnit)
            assertEquals(testCase.value, res)
        }
    }

    @Test
    fun givenWeightUnitIsPounds_testMultiplyIfPoundsAndRoundToNearestQuarter() {
        val givenToExpected = mutableMapOf(
            100f to 220.5f,
            46.11339603f to 101.75f,
            25.245251f to 55.75f,
            100.42363262f to 221.5f,
            95.6735f to 211,
            43.9353f to 97,
        )
        val weightUnit = POUNDS

        for (testCase in givenToExpected) {
            val res = testCase.key.multiplyIfPoundsAndRoundToNearestQuarter(weightUnit)
            assertEquals(testCase.value, res)
        }
    }
}