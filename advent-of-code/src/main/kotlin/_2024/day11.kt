package io.github.jangalinski.kata.aoc._2024

import io.github.jangalinski.kata.aoc.AocUtil.StringExt.longValues


private fun Long.numberOfDigits(): Int {
  var remain = this
  var digits = 0
  while (remain > 0) {
    digits++
    remain /= 10
  }
  return digits
}

private fun Long.splitHalf(size: Int = this.numberOfDigits()): Pair<Long, Long> {
  require(size % 2 == 0)
  var n = 1L
  repeat(size / 2) {
    n *= 10
  }

  return (this / n) to (this % n)
}

private fun Pair<Long, Long>.list(): List<Long> = listOf(first, second)

fun main() {

  // trick for many ops: store number and count
  fun read(n: Int): Map<Long, Long> {
    return when (n) {
      0 -> "0 1 10 99 999"
      1 -> "125 17"
      else -> "4610211 4 0 59 3907 201586 929 33750"
    }.longValues().groupingBy { it }.eachCount().map { it.key to it.value.toLong() }.toMap()
  }

  fun blink(n: Long): List<Long> {
    return when {
      n == 0L -> listOf(1L)
      n.numberOfDigits() % 2 == 0 -> n.splitHalf().list()
      else -> listOf(2024 * n)
    }
  }

  fun solve(map: Map<Long, Long>, i: Int): Long {
    return if (i == 0) {
      map.values.sum()
    } else {
      val x = buildMap {
        map.flatMap { e -> blink(e.key).map { it to e.value } }.forEach { p ->
          this.compute(p.first) { _, o -> (o ?: 0L) + p.second }
        }
      }
      solve(x, i - 1)
    }
  }

  val l = read(2)

  println(solve(l, 75))
}
