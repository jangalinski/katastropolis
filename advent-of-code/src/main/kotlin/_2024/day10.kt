package io.github.jangalinski.kata.aoc._2024

import io.github.jangalinski.kata.aoc.AocUtil
import io.github.jangalinski.kata.aoc.IntKrid
import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.CellValue
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
  fun read(type: Int): IntKrid {
    val string = when (type) {
      0 -> """
        0123
        1234
        8765
        9876
      """.trimIndent()

      1 -> """
        ...0...
        ...1...
        ...2...
        6543456
        7.....7
        8.....8
        9.....9
      """.trimIndent()

      2 -> """
        ..90..9
        ...1.98
        ...2..7
        6543456
        765.987
        876....
        987....
      """.trimIndent()

      3 -> """
        89010123
        78121874
        87430965
        96549874
        45678903
        32019012
        01329801
        10456732
      """.trimIndent()

      else -> AocUtil.Input(year = 2024, day = 10, part = 1, false).contentRaw
    }.trim()

    return krid(string, -1) { if (it == '.') -1 else it.digitToInt() }
  }

  fun findPath(k: IntKrid, input: List<CellValue<Int>>): List<Pair<Cell, Cell>> {
    require(input.isNotEmpty())

    // reached full path 0 to 9 -> hit
    if (input.size == 10) {
      return listOf(input.first().cell to input.last().cell)
    }

    // where are we now?
    val cur = input.last()

    // look for neighbor cells one bigger
    val next = k.orthogonalAdjacentCellValues(cur.cell)
      .filter { k.dimension.isInBounds(it.cell) }
      .filter { it.value == cur.value + 1 }

    // no neighbors?
    if (next.isEmpty()) {
      return emptyList()
    }

    // recurse neighbors
    return next.map { input + it }.flatMap { findPath(k, it) }
  }

  fun silver(k: IntKrid): Int {
    return k.cellValues().filter { it.value == 0 }
      .flatMap { findPath(k, listOf(it)) }
      .distinct()
      .groupingBy { it.first }.eachCount()
      .values.sum()
  }

  fun gold(k: IntKrid): Int {
    return k.cellValues().filter { it.value == 0 }
      .flatMap { findPath(k, listOf(it)) }
      .groupingBy { it.first }.eachCount()
      .values.sum()
  }

  println(gold(read(4)))
}
