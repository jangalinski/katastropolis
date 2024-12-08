package io.github.jangalinski.kata.project_euler

import java.util.stream.IntStream
import kotlin.streams.asSequence


fun main() {
  println(problem009())
}

private fun problem009(): Long {
  val s = sequence {
    (1L..1000L).forEach { a ->
      (1L..1000L).forEach { b ->
        val c = 1000 - a - b
        if (c > 0)
          yield(Triple(a, b, c))
      }
    }
  }
    .map {
      val (a, b, c) = it
      it to Triple(a * a, b * b, c * c)
    }.filter { it.second.first + it.second.second == it.second.third }
    .map { it.first }
    //.filter { it.first + it.second + it.third == 1000L }
    .filter { it.first <= it.second }

  return s.first().let { it.first * it.second * it.third }
}

private fun problem008(size: Int = 13): Long {
  val numbers = """
    73167176531330624919225119674426574742355349194934
    96983520312774506326239578318016984801869478851843
    85861560789112949495459501737958331952853208805511
    12540698747158523863050715693290963295227443043557
    66896648950445244523161731856403098711121722383113
    62229893423380308135336276614282806444486645238749
    30358907296290491560440772390713810515859307960866
    70172427121883998797908792274921901699720888093776
    65727333001053367881220235421809751254540594752243
    52584907711670556013604839586446706324415722155397
    53697817977846174064955149290862569321978468622482
    83972241375657056057490261407972968652414535100474
    82166370484403199890008895243450658541227588666881
    16427171479924442928230863465674813919123162824586
    17866458359124566529476545682848912883142607690042
    24219022671055626321111109370544217506941658960408
    07198403850962455444362981230987879927244284909188
    84580156166097919133875499200524063689912560717606
    05886116467109405077541002256983155200055935729725
    71636269561882670428252483600823257530420752963450
  """.trimIndent().lines().joinToString("", transform = String::trim)

  return numbers.windowed(size = size, partialWindows = false).maxOfOrNull { it.map { ch -> "$ch".toLong() }.fold(1L) { a, c -> a * c } }!!
}

private fun problem007() {
  // TODO - just searched in internet, prime number list
}

private fun problem006(num: Int = 100): Int {
  val (sq, s) = (1..num).map { it * it to it }.unzip()
  return s.sum() * s.sum() - sq.sum()
}

private fun problem005(num: Int = 20): Int {
  return when (num) {
    10 -> 1 * 2 * 3 * 2 * 5 * 1 * 7 * 2 * 3
    else -> 1 * 2 * 3 * 2 * 5 * 1 * 7 * 2 * 3 * 11 * 13 * 2 * 17 * 19
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
