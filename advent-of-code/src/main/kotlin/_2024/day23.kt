package io.github.jangalinski.kata.aoc._2024

import io.github.jangalinski.kata.aoc.AocUtil
import io.github.jangalinski.kata.aoc.AocUtil.ListExt.allPairs

fun main() {

  fun read(test: Boolean) = AocUtil.Input(year = 2024, day = 23, test = test).nonEmptyLines.map {
    val (x, y) = it.trim().split("-")
    x to y
  }

  fun mapping(input: List<Pair<String, String>>): Map<String, Set<String>> {
    return buildMap<String, Set<String>>() {
      input.forEach { (a, b) ->
        this.compute(a) { _, set -> if (set == null) setOf(b) else set + b }
        this.compute(b) { _, set -> if (set == null) setOf(a) else set + a }
      }
    }
  }


  fun silver(input: List<Pair<String, String>>): Int {
    val mapping = mapping(input)

    val allTriples = mapping.flatMap { (k, v) ->
      v.allPairs(true).map { p -> listOf(k, p.first, p.second).sorted() }
    }.filter { it.any { it.startsWith("t") } }
      .map { Triple(it[0], it[1], it[2]) }.toSet()

    fun isTriangle(t: Triple<String, String, String>): Boolean {
      return mapping[t.first]!!.containsAll(setOf(t.second, t.third)) &&
        mapping[t.second]!!.containsAll(setOf(t.first, t.third)) &&
        mapping[t.third]!!.containsAll(setOf(t.second, t.first))
    }

    return allTriples.filter { isTriangle(it) }.size
  }

  fun gold(input: List<Pair<String, String>>): String {
    val mapping = mapping(input)

//    ka-co
//    ta-co
//    de-co
//    ta-ka
//    de-ta
//    ka-de
    // co,de,ka,ta.
    mapping.forEach { println(it) }

    val x = mapping.map { (k,l) ->
      sequence {
        yield(k to l)
        l.forEach {
          yield(it to mapping[it]!!)
        }
      }.toSet()
    }

    x.forEach { println(it) }



    return ""
  }

  //println(silver(read(false)))
  println(gold(read(true)))
}
