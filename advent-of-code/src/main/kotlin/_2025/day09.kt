package io.github.jangalinski.kata.aoc._2025

import io.github.jangalinski.kata.aoc.AocUtil
import io.github.jangalinski.kata.aoc.Chrid
import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.ascii
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.Dimension
import kotlin.math.absoluteValue

fun main() {
  fun read(test:Boolean=false): Chrid = AocUtil.Input(year = 2025, day = 9, part = 1, test = test)
    .nonEmptyLines
    .map {
      val (a,b) = it.split(",").map(String::toInt)
      cell(a, b)
    }.let {
      val dim = Dimension(it.maxBy { it.x }.x + 3, it.maxBy { it.y }.y + 2)
      krid(dimension = dim, emptyElement = '.') {x,y -> if (it.contains(cell(x,y))) '#' else '.'}
    }

  fun allPairs(list: List<Cell>) : Sequence<Pair<Cell, Cell>> = sequence {
    for (a in list)
      for (b in list) {
        if (a == b) continue
        yield(setOf(a, b))
      }
  }.distinct()
    .map { it.toList().let { (a,b) -> a to b } }

  val k = read(false).let { krid ->
    //krid.rows().map { it.values.joinToString(separator = "") }.forEach { println(it) }
    krid
  }
  //println(k.ascii())

//  allPairs(read(false))
//    .map { it to ( (((it.first.x - it.second.x).absoluteValue) + 1).toLong() to (((it.first.y - it.second.y).absoluteValue) + 1).toLong() )}
//    .map { it to (it.second.first * it.second.second) }
//    .sortedBy { it.second }
//    .maxBy { it.second }
//    .let { println(it) }
    //.forEach { println(it) }
}
