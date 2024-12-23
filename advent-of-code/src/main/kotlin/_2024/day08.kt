package io.github.jangalinski.kata.aoc._2024

import io.github.jangalinski.kata.aoc.AocUtil.Input
import io.github.jangalinski.kata.aoc.Chrid
import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.CellValue
import io.toolisticon.lib.krid.model.step.CoordinatesStep

fun main() {

  fun read(test: Boolean = false): Chrid {
    val content = if (test) {
      """
        ............
        ........0...
        .....0......
        .......0....
        ....0.......
        ......A.....
        ............
        ............
        ........A...
        .........A..
        ............
        ............
      """.trimIndent()
    } else {
      Input(2024, 8, 1, false).contentRaw
    }
    return krid(content, '.')
  }

  fun Chrid.analyse(): Map<Char, List<CellValue<Char>>> {
    return cellValues().filterNot { it.value == '.' }.groupBy { it.value }
  }

  fun CoordinatesStep.inverse(): CoordinatesStep {
    return this.copy(cell(-this.x, -this.y))
  }

  fun deriveStep(a: Cell, b: Cell): CoordinatesStep {
    return CoordinatesStep(a.x - b.x, a.y - b.y)
  }

//
//  fun silver(k: Chrid): Int {
//    fun Chrid.antinodes(antennas: Pair<Cell, Cell>): Set<Cell> {
//      val s = deriveStep(antennas.first, antennas.second)
//
//
//      return setOf(
//        s(antennas.first),
//        s.inverse()(antennas.second)
//      ).filter(Dimension::isInBounds).toSet()
//    }
//
//    return k.analyse().flatMap { it.value.map { it.cell }.permutations() }
//      .flatMap(k::antinodes)
//      .distinct()
//      .count()
//  }
//
//  fun gold(k: Chrid): Int {
//    fun Chrid.antinodes(antennas: Pair<Cell, Cell>): Set<Cell> {
//      val s = deriveStep(antennas.first, antennas.second)
//
//      val before = generateSequence(antennas.first, s).takeWhile(Dimension::isInBounds).toSet()
//      val after = generateSequence(antennas.second, s.inverse()).takeWhile(Dimension::isInBounds).toSet()
//
//      return before + after
//    }
//
//    return k.analyse().flatMap { it.value.map { it.cell }.permutations() }
//      .flatMap(k::antinodes)
//      .distinct()
//      .count()
//  }
//
//
//  val krid = read()
//  println(silver(krid))
//  println(gold(krid))

}
