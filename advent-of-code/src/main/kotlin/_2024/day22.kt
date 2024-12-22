package io.github.jangalinski.kata.aoc._2024

import io.github.jangalinski.kata.aoc.AocUtil
import io.github.jangalinski.kata.aoc.AocUtil.StringExt.nonEmptyLines

fun main() {
  fun read(test: Boolean): List<Long> {
    return when (test) {
      true -> """
        1
        10
        100
        2024
      """.trimIndent()

      false -> AocUtil.Input(year = 2024, day = 22).contentRaw
    }.nonEmptyLines().map(String::trim).map(String::toLong)
  }

  fun Long.mix(other: Long): Long = this xor other
  fun Long.prune(): Long = this % 16777216L

  fun evolve(secret: Long): Long {
    var s = secret.mix(secret * 64).prune()
    s = (s / 32).mix(s).prune()
    return (s * 2048).mix(s).prune()
  }

  fun sequence(n: Long): Sequence<Triple<Long, Int, Int>> {
    return generateSequence(Triple(n, n.toInt() % 10, 0)) {
      val s = evolve(it.first)
      val p = s.toInt() % 10
      Triple(s, p, p - it.second)
    }
  }

  fun Sequence<Triple<Long, Int, Int>>.changesAndPrice(): Sequence<Pair<List<Int>, Int>> {
    return this.map { it.second to it.third }.windowed(size = 4, step=1).map {
      val (a,b) = it.unzip()
      b to a.last()
    }
  }

  fun priceChanges(n: Long): Sequence<Pair<String, Int>> {
//    val take = 2000
    val take = 10
    return sequence {
      yield(n.toInt() % 10)
      var s = n
      repeat(take) {
        s = evolve(s)
        yield(s.toInt() % 10)
      }
    }.take(take + 1).zipWithNext().map { (it.second - it.first) to it.first }
      .windowed(size = 4, step = 1)
      .map {
        val (a, b) = it.unzip()
        a.joinToString(separator = "#") to b.last()
      }
  }

  fun silver(input: List<Long>): Long {
    fun n(n: Long): Long = generateSequence(n) { evolve(it) }.take(2001).last()

    return input.sumOf { n(it) }
  }

  fun gold(input: List<Long>): Int {
    fun s(n:Long) = sequence(n).take(2000).changesAndPrice()
      .fold(mutableMapOf<String,Int>()) { a,c ->
        val key = c.first.toString()
        if (!a.containsKey(key)) {
          a.put(key, c.second)
        }
        a
      }.toList()
    val m = input.flatMap { s(it) }.groupBy({ it.first }, { it.second })

    return m.map { it.value.sum() }.max()
  }


  val input = read(false)

  println(gold(input))
}
