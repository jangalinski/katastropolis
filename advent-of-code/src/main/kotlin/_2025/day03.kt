package io.github.jangalinski.kata.aoc._2025

import io.github.jangalinski.kata.aoc.AocUtil.Input
import io.github.jangalinski.kata.aoc.AocUtil.StringExt.nonEmptyLines

fun main() {
  fun read(test: Boolean = false) = (if (test) """
    987654321111111
    811111111111119
    234234234234278
    818181911112111
  """.trimIndent() else Input(year = 2025, day = 3, part = 1, test = test).contentRaw).nonEmptyLines()

  fun joltage(line: String, size: Int = 2, result: Long = 0): Long = if (size == 0) {
    result
  } else {
    val next = size - 1
    val max = line.mapIndexed { i, it -> it.digitToInt() to i }
      .filter { it.second < line.length - next }
      .maxBy { it.first }
    joltage(line.substring(max.second + 1), next, result * 10 + max.first)
  }

  // SILVER
  val input = read(true)
  println("SILVER: ${input.sumOf { joltage(it, 2) }}")

  // GOLD
  println("GOLD: ${input.sumOf { joltage(it, 12) }}")
}
