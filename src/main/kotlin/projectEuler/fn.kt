package io.github.jangalinski.kata.projectEuler

import java.util.stream.IntStream
import kotlin.streams.asSequence


fun main() {
  println(problem006())
}



private fun problem006(num: Int=100): Int {
  val (sq, s) = (1..num).map { it * it to it }.unzip()
  return s.sum() * s.sum() - sq.sum()
}

private fun problem005(num: Int=20) : Int {
  return when(num) {
    10 ->  1 * 2 * 3 * 2 * 5 * 1 * 7 * 2 * 3
    else -> 1* 2*3*2*5*1*7*2*3*11*13*2*17*19
  }
}

private fun problem004(): Int {
  fun palindrome(x: Int): Boolean {
    val s = x.toString()
    return s == s.reversed()
  }

  return sequence {

    for (i in 101..999) {
      for (j in 101..999) {
        yield(i * j)
      }
    }
  }.filter(::palindrome).max()
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
