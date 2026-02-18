package io.github.jangalinski.kata.aoc._2025

import io.github.jangalinski.kata.aoc.AocUtil
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day05Test {

  fun List<LongRange>.simplify(): List<LongRange> {
    val sorted = this.sortedBy { it.first }
    val result = mutableListOf<LongRange>()
    var currentRange: LongRange? = null

    for (range in sorted) {
      if (currentRange == null) {
        currentRange = range
      } else {
        if (range.first <= currentRange.last + 1) {
          currentRange = currentRange.first..maxOf(currentRange.last, range.last)
        } else {
          result.add(currentRange)
          currentRange = range
        }
      }
    }

    if (currentRange != null) {
      result.add(currentRange)
    }

    return result
  }

  @Test
  fun gold() {
    // 347468726696961
    val input = AocUtil.Input(year = 2025, day = 5, part = 1, test = false).linesChunkedByEmpty()
      .first()
      .map { it.split("-").map(String::toLong).let { (a, b) -> a..b } }

    input.simplify().map {it.last - it.first + 1 }.sum().let { println(it) }
  }

  @Test
  fun name() {
    assertThat(listOf(1L..4L).simplify()).containsExactly(1L..4L)
    assertThat(listOf(1L..4L, 3L..5L).simplify()).containsExactly(1L..6L)
  }
}
