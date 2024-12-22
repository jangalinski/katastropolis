package io.github.jangalinski.kata.aoc._2024

import arrow.core.memoize
import io.github.jangalinski.kata.aoc.AocUtil
import io.github.jangalinski.kata.aoc.AocUtil.StringExt.chunkedByEmpty

fun main() {
  fun read(test: Boolean): Pair<List<String>, List<String>> {
    val content = when (test) {
      true -> """
        r, wr, b, g, bwu, rb, gb, br

        brwrr
        bggr
        gbbr
        rrbgbr
        ubwu
        bwurrg
        brgr
        bbrgwb
      """.trimIndent()

      false -> AocUtil.Input(year = 2024, day = 19, test = false).contentRaw
    }

    val (pattern, designs) = content.chunkedByEmpty()

    return pattern.single().split(", ") to designs.map { it.trim() }
  }

  val input = read(true)

  fun possible(s: String, patterns: Map<Char, List<String>>): Boolean {
    if (s.isEmpty()) {
      return true
    }


    val ps = (patterns[s.first()] ?: emptyList()).filter { s.startsWith(it) }
    if (ps.isEmpty()) {
      return false
    }

    return ps.map { possible(s.removePrefix(it), patterns) }
      .reduce { a, b -> a || b }
  }

  val mPossible = ::possible.memoize()

  fun silver(input: Pair<List<String>, List<String>>): Int {
    val patterns = input.first.groupBy { it.first() }

    return input.second.count { mPossible(it, patterns) }
  }

  fun gold(input: Pair<List<String>, List<String>>): Int {
    val patterns = input.first.groupBy { it.first() }

    val possibles = input.second.filter { mPossible(it, patterns) }
    println(possibles)

    fun variants(s:String) : Int {
      if (!mPossible(s, patterns) || s.isEmpty()) {
        return 0
      }
      val ps = (patterns[s.first()] ?: emptyList()).filter { s.startsWith(it) }
      if (ps.isEmpty()) {
        return  0
      }
      return ps.sumOf { variants(it) }
    }


    return 0
  }

  println(gold(input))
}
