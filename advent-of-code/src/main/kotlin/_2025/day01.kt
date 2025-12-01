package io.github.jangalinski.kata.aoc._2025

import io.github.jangalinski.kata.aoc.AocUtil
import io.github.jangalinski.kata.aoc.AocUtil.StringExt.nonEmptyLines
import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() {

  fun parse(input: String): List<Int> = input.trim().nonEmptyLines()
    .map { if (it.first() == 'R') it.drop(1).toInt() else -it.drop(1).toInt() }

  fun read(test: Boolean): List<Int> = parse(
    if (test) {
      """
      L68
      L30
      R48
      L5
      R60
      L55
      L1
      L99
      R14
      L82
    """.trimIndent()
    } else {
      AocUtil.Input(year = 2025, day = 1, part = 1, test).contentRaw
    }
  )

  fun silver(test: Boolean = false): Int = read(test).runningFold(50) { acc, v ->
    (acc + v).mod(100)
  }.count { it == 0 }

  fun gold(test: Boolean = false): Int = read(test)
    .flatMap { n -> IntArray(n.absoluteValue, { n.sign }).asIterable() }
    .runningFold(50) { acc, v -> (acc + v).mod(100) }
    .count { it == 0 }

  println(gold(false))
}
