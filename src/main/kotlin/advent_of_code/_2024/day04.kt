package io.github.jangalinski.kata.advent_of_code._2024

import io.github.jangalinski.kata.Katastropolis.KridExt.take
import io.github.jangalinski.kata.advent_of_code.AoCUtil
import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.get
import io.toolisticon.lib.krid.getValue
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.step.Direction


fun main() {

  fun read(test: Boolean = false, empty: Char = '.'): Krid<Char> {
    val rows = AoCUtil.Input(year = 2024, day = 4, part = 1, test = test)
      .nonEmptyLines.map { it.toList().map { it -> if (it == 'X') '.' else it } }
    return krid(rows, empty)
  }

  fun Cell.take(direction: Direction, count: Int): List<Cell> = this.take(direction, count, true)

  // find all 4-size beams that build the word XMAS
  fun silver(test: Boolean = false): Int {
    val krid = read(test)

    return krid.cells().flatMap() { cell ->
      Direction.entries.filterNot { Direction.NONE == it }
        .map { direction -> cell.take(direction, 4) }
        .filter { krid.dimension.filterInBounds(it).size == 4 }
    }.map { cells -> krid[cells].map { it.value }.joinToString("") }
      .count { "XMAS" == it }
  }

  // find all crosses of MAS, so the A is shared and opposite adjacent have values M,S
  fun gold(test: Boolean = false): Int {
    val krid = read(test)

    val aas = krid.cellValues().filter { it.value == 'A' }
      .map { value ->
        value to value.cell.adjacent.filter { cell -> krid.dimension.isInBounds(cell) }.map { krid.getValue(it) }
      }
      .filter { it.second.size == 8 }
      .map {
        it.second.map { it.value }.map { ch ->
          when (ch) {
            'M' -> true
            'S' -> false
            else -> null
          }
        }
      }
      .map {
        listOf(
          it[0] to it[4], // u-d
          it[1] to it[5], // ur-dl
          it[2] to it[6], // r-l
          it[3] to it[7], // dr-ul
        )
          .map { pair ->
            if (pair.first == null || pair.second == null) {
              false
            } else {
              pair.first!! xor pair.second!!
            }
          }
      }
      .map { (it[0] and it[2]) to (it[1] and it[3]) }
      .filter { it.first || it.second }

      .toList()

    aas.forEach { println(it)  }

    return aas.count()
  }

//  println(silver(true))
//  println(silver())

  println(gold())
}
