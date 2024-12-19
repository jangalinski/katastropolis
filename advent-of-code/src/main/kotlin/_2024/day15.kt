package io.github.jangalinski.kata.aoc._2024

import io.github.jangalinski.kata.aoc.AocUtil
import io.github.jangalinski.kata.aoc.AocUtil.StringExt.chunkedByEmpty
import io.github.jangalinski.kata.aoc.Chrid
import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.ascii
import io.toolisticon.lib.krid.model.step.Direction

fun main() {
  fun read(n: Int): Pair<Chrid, List<Direction>> {
    val (kstring, moves) = when (n) {
      0 -> """
        ########
        #..O.O.#
        ##@.O..#
        #...O..#
        #.#.O..#
        #...O..#
        #......#
        ########

        <^^>>>vv<v>>v<<
      """.trimIndent()
      1 -> """
        ##########
        #..O..O.O#
        #......O.#
        #.OO..O.O#
        #..O@..O.#
        #O#..O...#
        #O..O..O.#
        #.OO.O.OO#
        #....O...#
        ##########

        <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
        vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
        ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
        <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
        ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
        ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
        >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
        <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
        ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
        v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
      """.trimIndent()
      else -> AocUtil.Input(year = 2024, day = 15, test=false).contentRaw
    }.trim().split("\n\n")

    val directions = moves.lines().joinToString(separator = "") { it.trim() }.map {
      when(it) {
        '>' -> Direction.RIGHT
        '^' -> Direction.UP
        '<' -> Direction.LEFT
        'v' -> Direction.DOWN
        else -> null
      }
    }.filterNotNull()

    fun silver(input: Pair<Chrid, List<Direction>>) : Int {


      return 0
    }


    return krid(kstring, '.') to directions
  }

  val input = read(0)

  println(input.first.ascii())
  println(input.second)
}
