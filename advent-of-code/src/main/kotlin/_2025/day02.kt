package io.github.jangalinski.kata.aoc._2025

import io.github.jangalinski.kata.aoc.AocUtil
import kotlin.math.log10

fun main() {

  // alt+enter on regex
  val regex = Regex("""(\d+)\1+""")



  fun read(test: Boolean = false): List<LongRange> = AocUtil.Input(year = 2025, day = 2, test = test).nonEmptyLines
    .flatMap { it -> it.split(",").map { it.split("-") } }
    .map { (a, b) -> a.toLong()..b.toLong() }

  fun Long.split(size: Int = 0): List<Triple<Long, Int, Boolean>> {
    val digits = log10(this.toDouble()).toInt() + 1

    return if (size > 0) {
      if (size < digits && digits % size == 0) {
        listOf(Triple(this, digits / size, this.toString().chunked(size).distinct().size == 1))
      } else {
        listOf(Triple(this, 0, false))
      }
    } else {
      IntRange(1, digits - 1).flatMap { this.split(it) }
    }
  }

  fun LongRange.split(size: Int = 0): List<Triple<Long, Int, Boolean>> = this.asSequence().flatMap {
    it.split(size)
  }.toList()

  // silver:
  // read(false).flatMap { it.split() }.filter { it.third && it.second == 2}.sumOf { it.first }.let { println(it) }

  // gold
  read(false).flatMap { it.split() }.filter { it.third }
    .distinctBy { it.first }
    .sumOf { it.first }.let { println(it) }

}
