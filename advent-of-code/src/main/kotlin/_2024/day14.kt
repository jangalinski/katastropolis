package io.github.jangalinski.kata.aoc._2024

import io.github.jangalinski.kata.aoc.AocUtil
import io.github.jangalinski.kata.aoc.AocUtil.StringExt.nonEmptyLines
import io.github.jangalinski.kata.aoc.AocUtil.get
import io.github.jangalinski.kata.aoc.Chrid
import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.ascii
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.Dimension
import io.toolisticon.lib.krid.model.step.CoordinatesStep

fun main() {
  data class Robot(val position: Cell, val vector: CoordinatesStep, val dimension: Dimension) {
    fun step(num: Int): Robot {
      val calc = vector.times(num).invoke(position).let {
        Cell(it.x % dimension.width, it.y % dimension.height)
      }.let {
        if (dimension.isInBounds(it)) it else {
          Cell(
            if (it.x < 0) it.x + dimension.width else it.x,
            if (it.y < 0) it.y + dimension.height else it.y
          )
        }
      }

      return copy(position = calc)
    }

    val krid: Chrid by lazy {
      krid(dimension, '.') { x, y -> if (position.x == x && position.y == y) '1' else '.' }
    }
  }

  data class Bathroom(val dimension: Dimension, val robots: List<Robot>) {

    fun step(num: Int): Bathroom = copy(robots = robots.map { it.step(num) })

    val krid: Chrid by lazy {
      val cells = robots.map { it.position }.groupingBy { it }.eachCount()
      krid(dimension = dimension, '.') { x, y ->
        cells.getOrDefault(Cell(x, y), 0).let {
          when {
            it == 0 -> '.'
            it > 9 -> '*'
            else -> "$it"[0]
          }
        }
      }
    }

    private val x0 = IntRange(0,-1+dimension.width/2)
    private val x1 = IntRange(1+dimension.width/2,dimension.width-1)
    private val y0 = IntRange(0,-1+dimension.height/2)
    private val y1 = IntRange(1+dimension.height/2,dimension.height-1)

    val q1 = x0 to y0
    val q2 = x1 to y0
    val q3 = x0 to y1
    val q4 = x1 to y1

  }

  fun Pair<IntRange, IntRange>.contains(c:Cell) : Boolean {
    return first.contains(c.x) && second.contains(c.y)
  }

  fun read(test: Boolean): Bathroom {
    val regex = """p=(?<px>.+),(?<py>.+) v=(?<vx>.+),(?<vy>.+)""".toRegex()

    val content = when (test) {
      true -> """
        p=0,4 v=3,-3
        p=6,3 v=-1,-3
        p=10,3 v=-1,2
        p=2,0 v=2,-1
        p=0,0 v=1,3
        p=3,0 v=-2,-2
        p=7,6 v=-1,-3
        p=3,0 v=-1,-2
        p=9,3 v=2,3
        p=7,3 v=-1,2
        p=2,4 v=2,-3
        p=9,5 v=-3,-3
      """.trimIndent()

      false -> AocUtil.Input(year = 2024, day = 14, test = false).contentRaw
    }.trim()
    val dimension = Dimension(
      width = if (test) 11 else 101,
      height = if (test) 7 else 103
    )

    return Bathroom(
      dimension = dimension,
      robots = content.nonEmptyLines()
        .flatMap { line -> regex.findAll(line) }
        .map {
          Robot(
            position = Cell(it["px"].toInt(), it["py"].toInt()),
            vector = CoordinatesStep(it["vx"].toInt(), it["vy"].toInt()),
            dimension = dimension
          )
        }
    )
  }

  fun silver(input: Bathroom): Int {
    val n = input.step(100)


    val c = n.robots.map { it.position }.groupingBy {
      when  {
        input.q1.contains(it) -> 1
        input.q2.contains(it) -> 2
        input.q3.contains(it) -> 3
        input.q4.contains(it) -> 4
        else -> 0
      }
    }.eachCount()

    return c.filterNot { it.key == 0  }.map { it.value }.fold(1) { a,c -> a * c }
  }

  fun gold(input: Bathroom, num : Int): String{
    val n = input.step(num)


    return n.krid.ascii()
  }

  val input = read(false)

  println(gold(input, 10000))
}
