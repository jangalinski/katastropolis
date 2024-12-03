package io.github.jangalinski.kata.adventOfCode._2024

import io.github.jangalinski.kata.adventOfCode.AoCUtil
import java.util.regex.MatchResult
import java.util.regex.Pattern

fun main() {


  fun readAndMatch(test: Boolean = false, pattern: Pattern):List<MatchResult> = AoCUtil.Input(year = 2024, day = 3, part = 1, test = test)
    .nonEmptyLines
    .flatMap { pattern.matcher(it).results().toList() }

  fun silver(test: Boolean = false): Long {
    return readAndMatch(test, """mul\((\d+),(\d+)\)""".toPattern())
      .map { it.group(1).toLong() to it.group(2).toLong() }
      .sumOf { it.first * it.second }
  }

  fun gold(test: Boolean = false): Long {
    val r = """do\(\)|don't\(\)|mul\((\d+),(\d+)\)""".toRegex().toPattern()
    val l = AoCUtil.Input(year = 2024, day = 3, part = 1, test = test).nonEmptyLines
      .flatMap { r.matcher(it).results().toList() }
      .map {
        when (it.group()) {
          "don't()" -> false to 0L
          "do()" -> true to 0L
          else -> null to (it.group(1).toLong() * it.group(2).toLong())
        }
      }

    return l.fold(true to 0L) { acc, cur ->
      val enabled = cur.first ?: acc.first
      enabled to acc.second + (if (enabled) cur.second else 0L)
    }.second
  }

  println(gold())
  println(silver(true))
  println(silver())
}
