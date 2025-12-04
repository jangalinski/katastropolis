package io.github.jangalinski.kata.aoc._2025

import io.github.jangalinski.kata.aoc.AocUtil.Input
import io.github.jangalinski.kata.aoc.Chrid
import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.model.CellValue

fun main() {
  fun read(test: Boolean = false): Chrid = Input(year = 2025, day = 4, part = 1, test = test)
    .contentTrimmed.let { krid(string = it, emptyElement = '.') }

  fun Chrid.findBoxes(): Pair<Chrid, Int> {
    val isBox: (CellValue<Char>) -> Boolean = { cv -> cv.value == '@' }
    val hasLt4Neighbors: (CellValue<Char>) -> Boolean = { cv -> adjacentCellValues(cv.cell).filter { it.value == '@' }.size < 4 }
    val remove: (CellValue<Char>) -> CellValue<Char> = { cv -> cv.copy(value = '.') }

    val removedBoxes = cellValues()
      .filter(isBox)
      .filter(hasLt4Neighbors)
      .map(remove)
      .toList()
    return (this + removedBoxes) to removedBoxes.size
  }

  val krid = read(false)
  val sequence = generateSequence(krid.findBoxes()) { it.first.findBoxes() }

  // silver
  println(sequence.take(1).sumOf { it.second })

  // gold
  println(sequence.takeWhile { it.second > 0 }.sumOf { it.second })
}
