package io.github.jangalinski.kata.aoc._2025

import arrow.core.raise.result
import io.github.jangalinski.kata.aoc.AocUtil
import io.github.jangalinski.kata.aoc.AocUtil.StringExt.nonEmptyLines

fun main() {

  fun read(test: Boolean) : List<Pair<String,Int>> = if (test) {
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
  }.nonEmptyLines().map {
    val (d,v) = it.first().toString() to it.drop(1).toInt()
    d to v.toInt()
  }

  fun silver(test: Boolean = false) : Int = sequence {
    read(test).fold(50) { acc, (d,v) ->
      require(acc >= 0)
      val s = acc +  (if (d == "R") v else -v)
      val n = (if (s>=0) s else s+100) % 100
      if (n == 0)
        yield(n)
      n
    }
  }.count()


  println(silver(false))

//  Following these rotations would cause the dial to move as follows:
//
//  The dial starts by pointing at 50.
//  The dial is rotated L68 to point at 82.
//  The dial is rotated L30 to point at 52.
//  The dial is rotated R48 to point at 0.
//  The dial is rotated L5 to point at 95.
//  The dial is rotated R60 to point at 55.
//  The dial is rotated L55 to point at 0.
//  The dial is rotated L1 to point at 99.
//  The dial is rotated L99 to point at 0.
//  The dial is rotated R14 to point at 14.
//  The dial is rotated L82 to point at 32.


}
