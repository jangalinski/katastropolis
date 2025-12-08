package io.github.jangalinski.kata.aoc._2025

import io.github.jangalinski.kata.aoc.AocUtil
import io.github.jangalinski.kata.aoc.AocUtil.StringExt.nonEmptyLines

suspend fun main() {
  //fun read(test: Boolean = false): Pair<List<LongRange>, List<Long>>> = Input(year = 2025, day = 5, part = 1, test = test)
  fun read(test: Boolean = false): Pair<List<LongRange>, List<Long>> {
    val string = AocUtil.Input(year = 2025, day = 5, part = 1, test = test).contentTrimmed
    val (ranges, values) = string.split("\n\n")

    return ranges.nonEmptyLines().map {
      val (a, b) = it.split("-")
      a.toLong()..b.toLong()
    } to values.nonEmptyLines().map { it.toLong() }
  }

  val input = read()

  // silver
  //println(input.second.fold(0) {acc, cur -> if (input.first.any { cur in it }) acc + 1 else acc })

  // gold
  val ranges = input.first.sortedBy { it.first }
  println(ranges)
  val x = ranges.windowed(size = 2, step = 1).flatMap { (a, b) ->
    if (a.last > b.last) {
      listOf(a.first..a.last)
    } else if (a.last >= b.first) {
      listOf(a.first..b.first)
    } else {
      listOf(a, b)
    }
  }

  println(x)

  /*println(input.first.fold(setOf<Long>()) { acc, cur ->
    acc + cur.toSet()
  */
}
