package io.github.jangalinski.kata.aoc._2025

import io.github.jangalinski.kata.aoc.AocUtil
import io.github.jangalinski.kata.aoc.AocUtil.ListExt.head
import io.github.jangalinski.kata.aoc.AocUtil.StringExt.nonEmptyLines
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.model.Dimension

fun main() {
//  fun read(test:Boolean=false) = AocUtil.Input(year = 2025, day = 12, part = 1, test = test)
//    .contentTrimmed
//    .let {
//       val (rows, a) = it.split("\n\n").reversed().head()
//
//      a.reversed().map { it ->
//        val (i, k) = it.split(":")
//        val kr = Krids.krid(k)
//        kr to kr.cellValues().count { it.value == '#' }
//      } to rows.nonEmptyLines().map { r ->
//        val (d, s) = r.split(":")
//        val (x,y) = d.split("x").map(String::toInt)
//        val ns = s.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
//        Dimension(x,y) to ns
//      }
//
//    }
//
//  val (k,r) = read(test = true)
//
//  val sizes = k.map { it.second }
//
//  r.map { (d,l) ->
//    (d.width * d.height)  to l.zip(sizes).sumOf { (n,s) ->  n * (1+s) }
//  }.forEach { println(it) }
fun isPossible(area: Pair<Int, List<Int>>, presentSizes: List<Int>): Boolean {
  val availableArea = area.first
  val neededArea = area.second.mapIndexed { index, count -> presentSizes[index] * count }.sum()

  return neededArea.toDouble() < availableArea
}

  fun part1(input: List<String>): Int {
    val blocks = input.joinToString("\n").split("\n\n")
    val presents = blocks.take(blocks.size - 1)
    val areas = blocks.last()

    val presentSizes = presents.map { present -> present.count { it == '#' } }
    val parsedAreas = areas.split("\n").filter { it.isNotBlank() }.map { area ->
      val (size, presentCounts) = area.split(": ")
      val area = size.split("x").map { it.toInt() }.reduce { a, b -> a * b }
      val presentsAsList = presentCounts.split(" ").map { it.toInt() }
      area to presentsAsList
    }

    val possbible = parsedAreas.count { isPossible(it, presentSizes) }
    val impossible = parsedAreas.size - possbible

    println("Possible areas: $possbible")
    println("Impossible areas: $impossible")

    return input.size
  }

  val lines = AocUtil.Input(year = 2025, day = 12, part = 1, test = true).contentRaw.lines()
  //println(lines)
  println(part1(lines))

////    check(part1(testInput), 281)
////    check(part2(testInput), 281)
//
//  val input = readInput("day12/day12")
//  part1(input).println()
}
