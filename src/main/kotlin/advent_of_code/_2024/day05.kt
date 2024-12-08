package io.github.jangalinski.kata.advent_of_code._2024

import io.github.jangalinski.kata.Katastropolis.odd
import io.github.jangalinski.kata.advent_of_code.AoCUtil
import io.github.jangalinski.kata.advent_of_code.AoCUtil.StringExt.intValues
import io.github.jangalinski.kata.advent_of_code.AoCUtil.StringExt.toIntPair

fun main() {
  fun read(test: Boolean = false): Pair<List<Pair<Int, Int>>, List<List<Int>>> {
    val (por, updates) = AoCUtil.Input(year = 2024, day = 5, part = 1, test = test).linesChunkedByEmpty()

    return por.map(String::trim).map { it.toIntPair("|") } to updates.map { it.intValues() }
  }

  fun middleNumber(ints: List<Int>): Int {
    require(ints.size.odd()) { "$ints contains even number of elements." }
    return ints[ints.size / 2]
  }

  /**
   * Comparator based on Page Ordering Rules
   */
  fun comparator(p: List<Pair<Int, Int>>): Comparator<Int> {
    val map = p.groupBy({ it.first }, { it.second })
      .map { entry -> entry.key to entry.value.distinct().sorted() }.toMap()

//    println(map)

    return Comparator<Int> { a, b ->
      if (map[a]?.contains(b) == true) {
        -1
      } else if (map[b]?.contains(a) == true) {
        1
      } else {
        0
      }
    }
  }

  fun silver(test: Boolean = false): Int {
    val (comparator, updates) = read(test).let { comparator(it.first) to it.second }

    // pick lines not changed by sorting
    return updates.map { it to it.sortedWith(comparator) }
      .filter { it.first == it.second }
      .sumOf { middleNumber(it.first) }
  }

  fun gold(test: Boolean = false): Int {
    val (comparator, updates) = read(test).let { comparator(it.first) to it.second }

    // pick lines changed by sorting and sum sorted middle number
    return updates.map { it to it.sortedWith(comparator) }
      .filterNot { it.first == it.second }
      .sumOf { middleNumber(it.second) }
  }

  println(silver())
  println(gold())
}
