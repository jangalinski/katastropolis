package io.github.jangalinski.kata.aoc._2025

import io.github.jangalinski.kata.aoc.AocUtil
import io.github.jangalinski.kata.aoc.AocUtil.ListExt.head
import java.util.TreeSet

fun main() {
  fun String.indexes(char: Char) = sequence {
    this@indexes.forEachIndexed { index, c -> if (c == char) yield(index) }
  }.toList()

  fun read(test: Boolean=false) = AocUtil.Input(year = 2025, day = 7, part = 1, test = test).nonEmptyLines

//  sequence {
//    val (a, r) = read(true).head()
//
//  }

  fun silver(test: Boolean=false)= sequence {
    val (start, lines) = read(test).head()
    val beams = TreeSet<Int>().apply { (add(start.indexOf('S'))) }

    lines.map{it.indexes('^')}.filter { it.isNotEmpty() }.forEach { splitters ->
      val beamHitsSplitter = splitters.intersect(beams)
      yield(beamHitsSplitter.size)
      beamHitsSplitter.forEach {
        beams.remove(it)
        beams.add(it+1)
        beams.add(it-1)
      }
    }
  }.sum()

  println(silver())

}
