package io.github.jangalinski.kata.advent_of_code._2024

import io.github.jangalinski.kata.advent_of_code.AoCUtil
import kotlin.text.get

fun main() {
  operator fun MatchResult.get(name: String): Long = this.groups[name]?.value?.toLong() ?: 0L

  fun readAndMatch(test: Boolean = false, regex: Regex): List<MatchResult> = AoCUtil.Input(year = 2024, day = 3, part = 1, test = test)
    .nonEmptyLines
    .flatMap { regex.findAll(it) }


  fun silver(test: Boolean = false): Long {
    return readAndMatch(test, """mul\((?<a>\d{1,3}),(?<b>\d{1,3})\)""".toRegex())
      .map { it["a"] to it["b"] }
      .sumOf { it.first * it.second }
  }

  fun gold(test: Boolean = false): Long {
    val r = """do(n't)?\(\)|mul\((?<a>\d+),(?<b>\d+)\)""".toRegex()

    val l = readAndMatch(test, r)
      .map {
        when (it.value) {
          "don't()" -> false to 0L
          "do()" -> true to 0L
          else -> null to it["a"] * it["b"]
        }
      }

    return l.fold(true to 0L) { acc, cur ->
      val enabled = cur.first ?: acc.first
      enabled to acc.second + (if (enabled) cur.second else 0L)
    }.second
  }

  println(silver())
  println(gold())
}
