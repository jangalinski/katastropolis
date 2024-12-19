package io.github.jangalinski.kata.aoc._2024

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

  val input = read(false)


  fun silver(input: Pair<List<String>, List<String>>): Int {
    val patterns = input.first.groupBy { it.first() }

    val memoize = mutableMapOf<String, Boolean>().apply {
      put("", true)
      patterns.values.flatMap { it }.forEach { put(it, true) }
    }

    fun possible(s: String): Boolean {
      if (memoize.contains(s)) {
        return memoize[s]!!
      }

      val ps =( patterns[s.first()] ?: emptyList()).filter { s.startsWith(it) }
      if (ps.isEmpty()) {
        memoize.put(s, false)
        return false
      }

      return memoize.getOrPut(s) {  ps.map { possible(s.removePrefix(it)) }.reduce { a, b -> a || b } }
    }

    return input.second.count { possible(it) }
  }

  println(silver(input))
}
