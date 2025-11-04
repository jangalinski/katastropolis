package io.github.jangalinski.kata.onearmedbandit.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@Disabled
class CoinTest {
    @ParameterizedTest
    @CsvSource(
        // Addition
        "1,1,2",
        "2,3,5",
        "0,0,0",
        "5,0,5",
        // Subtraction
        "2,1,1",
        "2,2,0",
        "1,3,0",
        "10,5,5",
        // Edge cases
        "0,1,0",
        "1,0,1"
    )
    fun `coin addition and subtraction works and never goes below zero`(a: Int, b: Int, expected: Int) {
        val coinA = Coin(a)
        val coinB = Coin(b)
        val sum = coinA + coinB
        val diff = coinA - coinB
        assertThat(sum).isEqualTo(Coin(a + b))
        assertThat(diff).isEqualTo(Coin(expected))
    }

    @ParameterizedTest
    @CsvSource(
        "-1",
        "-5",
        "-100"
    )
    fun `creating a coin with negative value throws`(value: Int) {
        assertThatThrownBy { Coin(value) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Coin value must not be negative.")
    }
}
