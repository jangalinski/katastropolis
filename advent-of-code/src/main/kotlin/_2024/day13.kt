package io.github.jangalinski.kata.aoc._2024

import arrow.core.MemoizedDeepRecursiveFunction
import io.github.jangalinski.kata.aoc.AocUtil
import io.github.jangalinski.kata.aoc.AocUtil.StringExt.chunkedByEmpty
import io.github.jangalinski.kata.aoc.IntPair
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.step.CoordinatesStep
import kotlin.math.min

fun main() {
  fun read(test: Boolean): List<Triple<CoordinatesStep, CoordinatesStep, Cell>> {
    val content = when (test) {
      true -> """
        Button A: X+94, Y+34
        Button B: X+22, Y+67
        Prize: X=8400, Y=5400

        Button A: X+26, Y+66
        Button B: X+67, Y+21
        Prize: X=12748, Y=12176

        Button A: X+17, Y+86
        Button B: X+84, Y+37
        Prize: X=7870, Y=6450

        Button A: X+69, Y+23
        Button B: X+27, Y+71
        Prize: X=18641, Y=10279
      """.trimIndent()

      false -> AocUtil.Input(year = 2024, day = 13, part = 1, test = false).contentRaw
    }

    return content.chunkedByEmpty().map {
      val (a, b, p) = it.map { it.split("""\D+""".toRegex()).filter { it.trim().isNotEmpty() }.map(String::toInt) }
        .map { val (x, y) = it; x to y }
      Triple(CoordinatesStep(a.first, a.second), CoordinatesStep(b.first, b.second), Cell(p.first, p.second))
    }
  }

  fun silver(machines: List<Triple<CoordinatesStep, CoordinatesStep, Cell>>) : Int {
    fun solveMachine(
      machine: Triple<CoordinatesStep, CoordinatesStep, Cell>,
      start: Cell = Cell(0, 0),
      max: Int = 100
    ): List<IntPair> {
      val target = machine.third
      val stepA = machine.first
      val stepB = machine.second

      val maxA = min(max, min(target.x / stepA.x, target.y / stepA.y))
      val maxB = min(max, min(target.x / stepB.x, target.y / stepB.y))

      return IntRange(0, maxA).flatMap { a -> IntRange(0, maxB).map { a to it } }.filter {
        (stepA * it.first)(start) + (stepB * it.second)(start) == target
      }
    }

    return machines.map { solveMachine(it) }.filterNot { it.isEmpty() }.sumOf { it.minOf { it.first * 3 + it.second * 1 } }
  }



  fun gold(machines: List<Triple<CoordinatesStep, CoordinatesStep, Cell>>) : Int {
//    val offset = 10_000_000_000_000
//    return machines.map { solveMachine(it) }.filterNot { it.isEmpty() }.sumOf { it.minOf { it.first * 3 + it.second * 1 } }
      TODO()
  }

  val input = read(false)

  //println(silver(input))

  fun fibonacci(n: Int): Int {
    if (n <= 1) return n
    return fibonacci(n - 1) + fibonacci(n - 2)
  }
}
