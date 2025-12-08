package io.github.jangalinski.kata.aoc._2025

import io.github.jangalinski.kata.aoc.AocUtil.Input
import io.github.jangalinski.kata.aoc.AocUtil.ListExt.peek
import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.ascii
import org.junit.jupiter.api.Test

class Day06Test {
  @Test
  fun name() {
    val x = krid(string = Input(year = 2025, day = 6, part = 1, test = false)
      .contentRaw, emptyElement = ' ').columns().map { it.values }
    val k= krid(x, '.').ascii().split(".....")
      .map { krid(string=it,emptyElement='.') }
      .map { it.get(it.width-1,0) to it.subKrid(cell(0,0), cell(it.width-2,it.height-1)) }
      .map { it.first to it.second.rows().map { it.values.filter { it != '.' }.fold("") { a,c -> a+c} }}
      .map { it.first to it.second.map { it.toLong() }}

    k.map {
      if (it.first == '+')
        it.second.sum()
      else
        it.second.fold(1L) { acc, cur -> acc * cur }
    }.sum().also { println(it) }



  }
}
