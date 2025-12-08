package io.github.jangalinski.kata.aoc._2025

import io.github.jangalinski.kata.aoc.AocUtil.Input
import io.github.jangalinski.kata.aoc.AocUtil.ListExt.head
import io.github.jangalinski.kata.aoc.AocUtil.StringExt.nonEmptyLines
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.ascii

suspend fun main() {
  fun read(test: Boolean = false)= Input(year = 2025, day = 6, part = 1, test = test)
    .nonEmptyLines
    .reversed()
    .head()
    .let { (ops ,lines) ->


      //val indexes = ops.in(listOf("+","*"))
      val max = lines.maxOf { it.length }
      lines.map {
        it
      }
    }


  //val input = read(true)

  //input.forEach { println(it) }

  val k = krid(string = Input(year = 2025, day = 6, part = 1, test = true).contentRaw, emptyElement = ' ')
  println(k.ascii())


  // silver: 6343365546996
//  println(input.second.let { Krids.krid(it, 0L) }.columns().map { it.values.reversed() }
//    .zip(input.first).sumOf { (l, op) ->
//      if (op == "+")
//        l.sum()
//      else
//        l.fold(1L) { acc, cur -> acc * cur }
//    })
}
