package io.github.jangalinski.kata.aoc._2025

import io.github.jangalinski.kata.aoc.AocUtil
import io.github.jangalinski.kata.aoc.AocUtil.StringExt.nonEmptyLines
import org.junit.jupiter.api.Test

class Aoc2025_01Test {

  fun read(test: Boolean = false): List<Pair<String, Int>> = if (test) {
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
    val (d, v) = it.first().toString() to it.drop(1).toInt()
    d to v.toInt()
  }

  fun List<Pair<String, Int>>.dial(start: Int = 50) = sequence {
    fold(start) { acc, (d, v) ->
      require(acc >= 0)
      val va = v % 100
      val s = acc + (if (d == "R") va else -va)
      val n = (if (s >= 0) s else s + 100) % 100
      yield(n)
      n
    }
  }

  @Test
  fun silver() {
    val res = read().dial().count { it == 0 }
    println(res)
  }

  @Test
  fun gold() {
    val clicks = read().flatMap { p -> IntArray(p.second) { if (p.first == "R") 1 else -1 }.toList() }

    val s = sequence {

      clicks.fold(50) { acc, c ->
        val n = acc + c
        val v = if (n < 0) {
          99
        } else if (n > 99) {
          0
        } else {
          n
        }
        yield(v)
        v
      }
    }
    println(s.count { it == 0 })
  }
}
