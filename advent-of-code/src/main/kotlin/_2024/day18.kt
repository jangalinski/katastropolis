package io.github.jangalinski.kata.aoc._2024

import io.github.jangalinski.kata.aoc.AocUtil
import io.github.jangalinski.kata.aoc.AocUtil.StringExt.intValues
import io.github.jangalinski.kata.aoc.Chrid
import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.ascii
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.Dimension
import kotlin.math.abs

fun main() {

  fun read(test: Boolean): Chrid {
    val num = if (test) 12 else 1024
    val bytes = AocUtil.Input(year = 2024, day = 18, part = 1, test = test).nonEmptyLines.asSequence().map {
      val (x, y) = it.intValues()
      Cell(x, y)
    }.take(num).toSet()
    val dimension = if (test) Dimension(7, 7) else Dimension(70, 70)

    return krid(dimension = dimension, '.') { x, y -> if (bytes.contains(Cell(x, y))) '#' else '.' }
  }

  fun astar(krid: Chrid, start: Cell, end: Cell): Int {
    // g (from start) to h (to end)
    fun Pair<Int, Int>.f(): Int = first + second
    fun manhattanDistance(a: Cell, b: Cell): Int = abs(b.x - a.x) + abs(b.y - a.y)

    val openList = mutableMapOf<Cell, Pair<Pair<Int, Int>, Cell?>>()
    val closeList = mutableMapOf<Cell, Cell?>()

    openList[start] = (0 to manhattanDistance(start, end)) to null

    var last: Cell = sequence {
      while (openList.isNotEmpty()) {
        val (cell, way) = openList.minBy { it.value.first.f() }.also { openList.remove(it.key) }
        if (cell == end) {
          closeList[cell] = way.second
          yield(cell)
          break
        }
        val adjacent = krid.orthogonalAdjacentCellValues(cell)
          .filter { krid.dimension.isInBounds(it.cell) }
          .filter { it.value == '#' }
          .map {
            val cost = manhattanDistance(it.cell, start) to manhattanDistance(it.cell, end)
            it.cell to (cost to cell)
          }

        adjacent.forEach {
          if (!closeList.contains(it.first)) {
            openList[it.first] = it.second
          }
        }
        closeList[cell] = way.second
      }
    }.single()

    return generateSequence(last) { closeList[it] }
      .count() -1
  }


  val input = read(false)
  println(input.ascii())
  println(input.get(70,70))
  println(input.cellValues().count { it.value == '#' })

  println(astar(input, Cell(0, 0), Cell(input.dimension.width-1, input.dimension.height-1)))

}
