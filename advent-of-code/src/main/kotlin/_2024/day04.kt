package io.github.jangalinski.kata.aoc._2024

import io.github.jangalinski.kata.aoc.AocUtil.Input
import io.github.jangalinski.kata.aoc.AocUtil.KridExt.take
import io.github.jangalinski.kata.aoc.Chrid
import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.ascii
import io.toolisticon.lib.krid.get
import io.toolisticon.lib.krid.getValue
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.CellValue
import io.toolisticon.lib.krid.model.adjacent
import io.toolisticon.lib.krid.model.step.Direction

fun main() {

  fun read(test: Boolean = false, empty: Char = '.'): Chrid {
    val content = when(test) {
      true -> """
        MMMSXXMASM
        MSAMXMSMSA
        AMXSXMAAMM
        MSAMASMSMX
        XMASAMXAMM
        XXAMMXXAMA
        SMSMSASXSS
        SAXAMASAAA
        MAMMMXMMMM
        MXMXAXMASX
      """.trimIndent()
      false -> Input(year = 2024, day = 4, part = 1, test = false).contentRaw
    }
    return Krids.krid(content, empty)
  }

  fun Cell.take(direction: Direction, count: Int): List<Cell> = this.take(direction, count, true)

  // find all 4-size beams that build the word XMAS
  fun silver(k: Chrid): Int {
    return k.cells().flatMap() { cell ->
      Direction.entries.filterNot { Direction.NONE == it }
        .map { direction -> cell.take(direction, 4) }
        .filter { k.dimension.filterInBounds(it).size == 4 }
    }.map { cells -> k[cells].map { it.value }.joinToString("") }
      .count { "XMAS" == it }
  }

  // find all crosses of MAS, so the A is shared and opposite adjacent have values M,S
  fun gold(_k:Chrid): Int {
    val k = krid(_k.ascii{ if ('X' == it) '.' else it })

    return k.cellValues().filter { 'A' == it.value }
      .map { it to it.cell.adjacent(Direction.UP_LEFT, Direction.UP_RIGHT, Direction.DOWN_RIGHT, Direction.DOWN_LEFT)
        .filter { k.dimension.isInBounds(it) }
        .map { k.getValue(it) }
      }.filter { it.second.size == 4 }
      .map {
        "${it.second[0].value}${it.first.value}${it.second[2].value}" to "${it.second[1].value}${it.first.value}${it.second[3].value}"
      }.filter {
        fun String.isMas() = "MAS" == this || "SAM" == this

        it.first.isMas() && it.second.isMas()
      }.count()
  }

  val k = read(false)

  println(gold(k))
}
