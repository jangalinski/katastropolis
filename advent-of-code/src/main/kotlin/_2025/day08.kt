package io.github.jangalinski.kata.aoc._2025

import io.github.jangalinski.kata.aoc.AocUtil
import io.github.jangalinski.kata.aoc.AocUtil.ListExt.peekPrint
import kotlin.math.pow

fun main() {
  infix fun Triple<Long, Long, Long>.distance(other: Triple<Long, Long, Long>): Double =
    (this.first - other.first).toDouble().pow(2) + (this.second - other.second).toDouble().pow(2) + (this.third - other.third).toDouble()
      .pow(2)

  fun read(test: Boolean = false) = AocUtil.Input(year = 2025, day = 8, part = 1, test = test).nonEmptyLines
    .map { it.trim() }
    .map { it.split(',').map(String::toLong) }
    .map { Triple(it[0], it[1], it[2]) }
    .sortedBy { it.first + it.second + it.third }

  val test = true
  val input = read(test)

  fun allPairs() : Sequence<Set<Triple<Long, Long, Long>>> = sequence {
    val seen = mutableSetOf<Triple<Long, Long, Long>>()
    for (a in input)
      for (b in input) {
        if (a == b) continue

        yield(Pair(setOf(a, b), a distance b))
      }
  }.sortedBy { it.second }
    .distinctBy { it.first }
    .map { it.first }


  fun silver(): Int {
    val result = mutableSetOf<MutableSet<Triple<Long, Long, Long>>>()
    allPairs()
      .take((if (test) 10 else 1000))
      .forEach { s ->
        if (result.any { it.containsAll(s) }) return@forEach
        result.removeIf { it.isEmpty() }

        val (a, b) = s.toList()
        val setA = result.find { it.contains(a) }
        val setB = result.find { it.contains(b) }

        // merge sets
        if (setA != null && setB != null) {
          result.add(setA.union(setB).toMutableSet())
          setA.clear()
          setB.clear()
        } else if (setA != null) {
          setA.add(b)
        } else if (setB != null) {
          setB.add(a)
        } else {
          result.add(mutableSetOf(a, b))
        }
      }

    return result
      .map { it.size }.filter { it > 0 }
      .distinct()
      .sorted()
      .reversed()
      .take(3)
      .fold(1) { a, c -> a * c }
  }

//  fun gold(): List<Set<Triple<Long, Long, Long>>> = allPairs().toList()
  allPairs().forEach { println(it) }
}
