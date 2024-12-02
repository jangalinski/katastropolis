package io.github.jangalinski.kata.adventOfCode._2024

import io.github.jangalinski.kata.adventOfCode.AoCUtil
import io.github.jangalinski.kata.adventOfCode.AoCUtil.StringExt.intValues
import kotlin.math.abs

fun main() {
  fun read(test: Boolean = false): Pair<List<Int>, List<Int>> {
    val linesAsInts = AoCUtil.Input(year = 2024, day = 1, part = 1, test = test).nonEmptyLines
      .map { it -> it.intValues() }
      .map { it[0] to it[1]  }
    return linesAsInts.map {it.first}.sorted() to  linesAsInts.map {it.second}.sorted()
  }


  fun silver(test: Boolean = false): Int {
    val (l,r) = read(test)
    return l.zip(r).sumOf { abs(it.second - it.first) }
  }

  fun gold(test: Boolean = false) : Int {
    val (l,r) = read(test)

    val x = r.groupingBy { it  }.eachCount()

    return l.map { it to x.getOrDefault(it,0)  }.sumOf { it.second * it.first }
  }

  //println(silver())
  println(gold())


}
