package io.github.jangalinski.kata.adventOfCode._2024

import io.github.jangalinski.kata.Katastropolis.head
import io.github.jangalinski.kata.adventOfCode.AoCUtil

fun main() {

  val operations: Map<Int, (Long, Long) -> Long> = mapOf(
    0 to Long::plus,
    1 to Long::times,
    2 to { a, b -> "$a$b".toLong() },
  )

  // 0: +
  // 1: *
  // 2: ||
  fun operators(size: Int, radix: Int): List<List<Int>> {
    require(radix > 1)
    val max = "${radix - 1}".repeat(size)

    return generateSequence(0) { it + 1 }
      .map { it.toString(radix).padStart(size, '0') }
      .takeWhile { it != max }
      .toList().plus(max)
      .map {
        it.map {
          it.digitToInt()
        }
      }
  }

  fun read(test: Boolean = false): List<Pair<Long, List<Long>>> {
    return if (test) {
      """190: 10 19
      3267: 81 40 27
      83: 17 5
      156: 15 6
      7290: 6 8 6 15
      161011: 16 10 13
      192: 17 8 14
      21037: 9 7 18 13
      292: 11 6 16 20
    """.trimIndent().lines()
    } else {
      AoCUtil.Input(year = 2024, day = 7, part = 1, false).nonEmptyLines
    }.map { it.trim() }.filterNot { it.isEmpty() }
      .map { it.split("""\D+""".toRegex()).map(String::toLong) }
      .map { it.head() }
  }

  fun lineCalc(nums: List<Long>, radix: Int): Sequence<Long> {
    return sequence {
      operators(nums.size - 1, radix).forEach {
        val ops = listOf(0) + it
        yield(nums.zip(ops).fold(0L) { a, c ->
          operations[c.second]!!(a, c.first)
        })
      }
    }
  }

  fun silver(input: List<Pair<Long, List<Long>>>): Long {
    return input.map {
      it.first to lineCalc(it.second, 2).firstOrNull { r -> r == it.first }
    }.filter { it.second != null }.sumOf { it.first }
  }

  fun gold(input: List<Pair<Long, List<Long>>>): Long {
    return input.map {
      it.first to lineCalc(it.second, 3).firstOrNull { r -> r == it.first }
    }.filter { it.second != null }.sumOf { it.first }
  }

  val input = read()

  println(silver(input))
  println(gold(input))
}
