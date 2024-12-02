package io.github.jangalinski.kata.adventOfCode._2024

import io.github.jangalinski.kata.Katastropolis.println
import io.github.jangalinski.kata.adventOfCode.AoCUtil
import io.github.jangalinski.kata.adventOfCode.AoCUtil.StringExt.intValues
import kotlin.math.abs
import kotlin.math.sign

fun main() {
  fun read(test: Boolean = false) = AoCUtil.Input(year = 2024, day = 2, part = 1, test = test)
    .nonEmptyLines.map { it.intValues() }

  fun validate(ints: List<Int>) : Boolean {
    val w = ints.windowed(2).map { it -> it[0]-it[1] }
    val contains0 = w.contains(0)
    val changesDirection = w.map { it.sign  }.distinct().size !=1
    val toHighMax = w.maxOf(::abs) > 3

    return !contains0 && !changesDirection && !toHighMax
  }

  fun validate(ints: List<List<Int>>) : Boolean {
    return ints.fold(false) { acc, cur -> acc || validate(cur) }
  }


  fun iterate(ints: List<Int>): List<List<Int>> {
    return sequence{
      yield(ints)
      ints.indices.forEach { i ->
        yield(ints.toMutableList().apply{ removeAt(i) })
      }
    }.toList()
  }

  fun silver(test: Boolean = false): Int {
    //println(read(test))
    val x= read(test)
      .filter(::validate)

    return x.size
  }

  fun gold(test: Boolean = false): Int {
    val x = read(test).map { iterate(it)}


    return x.map { it ->  validate(it) }.count { it == true}
  }

  println(silver())
  println(gold())

}
