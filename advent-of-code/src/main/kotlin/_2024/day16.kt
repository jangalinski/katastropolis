package io.github.jangalinski.kata.aoc._2024

import io.github.jangalinski.kata.aoc.AocUtil
import io.github.jangalinski.kata.aoc.Chrid
import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.get
import io.toolisticon.lib.krid.getValue
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.CellValue
import io.toolisticon.lib.krid.model.adjacent
import io.toolisticon.lib.krid.model.step.Direction
import kotlin.math.min

fun main() {
  data class R(val direction: Direction, val cell: Cell)

  fun read(n: Int): Chrid {
    return when (n) {
      0 -> """
        ###############
        #.......#....E#
        #.#.###.#.###.#
        #.....#.#...#.#
        #.###.#####.#.#
        #.#.#.......#.#
        #.#.#####.###.#
        #...........#.#
        ###.#.#####.#.#
        #...#.....#.#.#
        #.#.#.###.#.#.#
        #.....#...#.#.#
        #.###.#.#.#.#.#
        #S..#.....#...#
        ###############
      """.trimIndent()

      1 -> """
        #################
        #...#...#...#..E#
        #.#.#.#.#.#.#.#.#
        #.#.#.#...#...#.#
        #.#.#.#.###.#.#.#
        #...#.#.#.....#.#
        #.#.#.#.#.#####.#
        #.#...#.#.#.....#
        #.#.#####.#.###.#
        #.#.#.......#...#
        #.#.###.#####.###
        #.#.#...#.....#.#
        #.#.#.#####.###.#
        #.#.#.........#.#
        #.#.#.#########.#
        #S#.............#
        #################
      """.trimIndent()

      else -> AocUtil.Input(year = 2024, day = 16, test = false).contentRaw
    }.let {
      krid(it, '.')
    }
  }

  fun silver(input: Chrid): Int {
    fun Triple<Int,Int,Int>.min() = min(first, min(second, third))

    fun left(d: Direction) = when(d) {
      Direction.UP -> Direction.LEFT
      Direction.LEFT -> Direction.DOWN
      Direction.DOWN -> Direction.RIGHT
      else -> Direction.UP
    }
    fun right(d: Direction) = when(d) {
      Direction.UP -> Direction.RIGHT
      Direction.LEFT -> Direction.UP
      Direction.DOWN -> Direction.LEFT
      else -> Direction.DOWN
    }
    fun CellValue<Char>.possible(): Boolean {
      return this.value != '#'
    }

    val start = R(Direction.RIGHT, input.cellValues().find { it.value == 'S' }!!.cell)




    fun walk(r: R, way: List<R> = emptyList(), points:Int= 0) : Int {
      if (way.count { it == r } > 1)
        return Int.MAX_VALUE

      if (input[r.cell] == 'E')
        return points

      //adjacent(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT)
      val fw = input.getValue(r.direction(r.cell))
      val tl = input.getValue(left(r.direction)(r.cell))
      val tr = input.getValue(right(r.direction)(r.cell))


      return Triple(
        if (fw.value != '#') {
          walk(r.copy(cell = fw.cell), way = way + r.copy(cell = fw.cell), points = points +1)
        } else {
          Int.MAX_VALUE
        },
        if (tl.value != '#') {
          walk(r.copy(cell = tl.cell, direction = left(r.direction)), way = way + r.copy(cell = tl.cell, direction = left(r.direction)), points = points +1001)
        } else {
          Int.MAX_VALUE
        },
        if (tr.value != '#') {
          walk(r.copy(cell = tl.cell, direction = right(r.direction)), way = way + r.copy(cell = tl.cell, direction = right(r.direction)), points = points +1001)
        } else {
          Int.MAX_VALUE
        }
      ).min()
    }

    return walk(start)
  }

  val input = read(0)

  println(Int.MAX_VALUE)
  println(silver(input))
}
