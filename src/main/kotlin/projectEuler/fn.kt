package io.github.jangalinski.kata.projectEuler

import java.util.stream.IntStream
import kotlin.streams.asSequence


fun main() {
  println(problem003(600851475143))
}

private fun problem004(digits:Int = 3): Int {

  return 0
}

private fun problem003(num: Long = 600851475143): Long {
  fun primes(n: Long): Pair<Long, Long> {
    for (i in 2 until n / 2 + 1) {
      if (n % i == 0L) {
        return i to n / i
      }
    }
    return 1L to n
  }

  var n = primes(num)
  val factors = mutableListOf<Long>(1)

  while (n.first != 1L) {
    factors.add(n.first)
    n = primes(n.second)
    if (n.first == 1L) {
      factors.add(n.second)
    }
  }

  return factors.max()
}

private fun problem002(limit: Int = 4_000_000): Int {
  return generateSequence(0 to 1) {
    it.second to it.first + it.second
  }.takeWhile { it.second <= limit }
    .map(Pair<Int, Int>::second)
    .filter { it % 2 == 0 }
    .sum()
}

/**
 * If we list all the natural numbers below that are multiples of 3
 * or 5, we get and. The sum of these multiples is 23.
 *
 * Find the sum of all the multiples of 3 or 5 below 1000.
 */
private fun problem001(limit: Int = 1000): Int {
  return IntStream.range(0, limit).asSequence()
    .filter { it % 3 == 0 || it % 5 == 0 }
    .sum()
}
