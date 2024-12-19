package io.github.jangalinski.kata.aoc

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class MathTest {

  @ParameterizedTest
  @CsvSource(
    value = [
      "0,0,1",
      "1,0,1",
      "1,1,1",
      "1,2,1",
      "2,0,1",
      "2,1,2",
      "2,3,8",
    ]
  )
  fun `int power`(base: Int, exponent: Int, expected: Int) {
    assertThat(base.pow(exponent)).isEqualTo(expected)
  }

}
