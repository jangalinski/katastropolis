package io.github.jangalinski.kata.aoc._2024

import io.github.jangalinski.kata.aoc.AocUtil
import io.github.jangalinski.kata.aoc.AocUtil.KridExt.beam
import io.github.jangalinski.kata.aoc.Chrid
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.CellValue
import io.toolisticon.lib.krid.model.step.Direction.*

fun main() {
  // read example Krid<Char> or input
  fun read(n: Int): Chrid {
    val content = when (n) {
      0 -> """
        AAAA
        BBCD
        BBCC
        EEEC
      """.trimIndent()

      1 -> """
        OOOOO
        OXOXO
        OOOOO
        OXOXO
        OOOOO
      """.trimIndent()

      2 -> """
        RRRRIICCFF
        RRRRIICCCF
        VVRRRCCFFF
        VVRCCCJFFF
        VVVVCJJCFE
        VVIVCCJJEE
        VVIIICJJEE
        MIIIIIJJEE
        MIIISIJEEE
        MMMISSJEEE
      """.trimIndent()

      else -> AocUtil.Input(year = 2024, day = 12, part = 1, test = false).contentRaw.trim()
    }
    return Krids.krid(content, '.')
  }

  // collect an area by recursivley adding cell values that are orthogonally adjacent.
  tailrec fun findArea(k: Chrid, seeds: List<CellValue<Char>>, area: Set<CellValue<Char>> = emptySet()): Set<CellValue<Char>> {
    require(seeds.map { it.value }.distinct().size < 2) { "all values must be the same: $seeds" }
    if (seeds.isEmpty()) return area

    val newArea = area + seeds

    val adjacents = seeds.flatMap { k.orthogonalAdjacentCellValues(it.cell) }
      .filter { it.value == seeds.first().value }
      .distinct()
      .filterNot { newArea.contains(it) }

    return findArea(k, adjacents, newArea)
  }

  // collect all areas by calling findArea per each cellValue not already in an area
  fun findAllAreas(k: Chrid): List<Set<CellValue<Char>>> {
    return buildList {
      k.cellValues().forEach { c ->
        if (this.none { a -> a.contains(c) }) {
          add(findArea(k, listOf(c)))
        }
      }
    }
  }

  // area is size of elements
  fun area(a: Set<CellValue<Char>>): Int = a.size

  // perimeter silver - count all outer cells
  fun perimeterSilver(a: Set<CellValue<Char>>): Int {
    val cells = a.map { it.cell }.toSet()

    return cells.map { c -> c.orthogonalAdjacent.filterNot { cells.contains(it) } }.sumOf { it.size }
  }

  // parameter gold: for each pair of inner/outer remove all cells on a "beam" up/down and left/right
  fun perimeterGold(a: Set<CellValue<Char>>): Int {

    // map each inner with its outer cell
    val outer = buildSet {
      a.map { it.cell }.toSet()
        .map { c -> c to c.orthogonalAdjacent.filterNot { oa -> a.any { it.cell == oa } } }
        .flatMap { c -> c.second.map { c.first to it } }
        .forEach { add(it) }
    }.toMutableSet()

    // find all fence elements per direction
    fun removeCellsOnFenceBeam(cell: Pair<Cell, Cell>): Sequence<Pair<Cell, Cell>> = sequenceOf(LEFT, RIGHT, UP, DOWN)
      .map { direction ->
        // each direction: build pair of inner/outer beam and take all fence elements
        cell.first.beam(direction, false).zip(cell.second.beam(direction, false))
          .takeWhile { outer.contains(it) }
      }.reduce { a, b -> a + b }

    return sequence {
      // until the outer set is empty
      while (outer.isNotEmpty()) {
        val cell = outer.first()
        // remove all cells on fence elements
        removeCellsOnFenceBeam(outer.first()).forEach { outer.remove(it) }
        outer.remove(cell)
        // count side
        yield(1)
      }
    }.sum()
  }

  fun silver(k: Chrid): Int = findAllAreas(k).sumOf { area(it) * perimeterSilver(it) }

  fun gold(k: Chrid): Int = findAllAreas(k).sumOf { area(it) * perimeterGold(it) }

  val k = read(3)

  println(silver(k))
  println(gold(k))
}
