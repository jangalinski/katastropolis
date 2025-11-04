package io.github.jangalinski.kata.onearmedbandit.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@Disabled
class ModelTest {

    @ParameterizedTest
    @CsvSource(
        // Winning cases
        "APPLE, APPLE, APPLE, 10",
        "BANANA, BANANA, BANANA, 15",
        "CLEMENTINE, CLEMENTINE, CLEMENTINE, 20",
        // No win
        "APPLE, BANANA, CLEMENTINE, 0",
        "BANANA, APPLE, CLEMENTINE, 0",
        "APPLE, BANANA, BANANA, 0"
    )
    fun `payout is calculated correctly`(first: SlotWheel, second: SlotWheel, third: SlotWheel, expected: Int) {
        val result = SlotResult(first = first, second = second, third = third)
        assertThat(result.payout).isEqualTo(expected)
    }
}
