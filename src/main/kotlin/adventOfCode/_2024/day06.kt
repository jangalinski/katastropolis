package io.github.jangalinski.kata.adventOfCode._2024

import io.github.jangalinski.kata.Katastropolis.KridExt.turn
import io.github.jangalinski.kata.adventOfCode.AoCUtil
import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.getValue
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.step.Direction
import io.toolisticon.lib.krid.model.step.Direction.RIGHT
import io.toolisticon.lib.krid.plus

fun main() {
  fun read(test: Boolean = false): Pair<Krid<Char>, Pair<Direction, Cell>> {
    val krid = krid(AoCUtil.Input(year = 2024, day = 6, part = 1, test = test).contentTrimmed, '.')
    return krid to krid.cellValues().single { it.value == '^' }.let { Direction.UP to it.cell }
  }

  fun silver(test: Boolean = false): Int {
    val i = read(test)
    val k = i.first
    var g = i.second

    return sequence {
      while (k.dimension.isInBounds(g.second)) {
        yield(g)

        val forward = g.first(g.second)
        val block: Boolean = if (k.dimension.isInBounds(forward)) {
          k.getValue(forward).value == '#'
        } else {
          false
        }

        g = when (block) {
          // no block, just walk
          false -> g.first to forward
          // block: turn
          true -> g.first.turn(RIGHT) to g.second
        }
      }
    } .map { it.second }

      .distinct()
      .count()
  }

  fun gold(test: Boolean = false): Int {
    val i = read(test)
    val k = i.first

    // walk mace and return true if loop detected
    fun walk(k: Krid<Char>, g: Pair<Direction, Cell>) : Boolean {
      var direction = g.first
      var position = g.second
      // if we pass a cell in the same direction we already did, it must be a loop
      val visited = mutableSetOf<Pair<Direction, Cell>>()

      while (k.dimension.isInBounds(position)) {
        if (!visited.add(direction to position)) {
          return true
        }
        val forward = direction(position)
        val block: Boolean = if (k.dimension.isInBounds(forward)) {
          k.getValue(forward).value == '#'
        } else {
          false
        }

        when (block) {
          // no block, just walk
          false -> position = forward
          // block: turn
          true -> direction = direction.turn(RIGHT)
        }
      }

      return false
    }

    return k.cellValues().filter { it.value != '#'  }
      .map { k.plus(it.copy(value = '#') ) }
      .filter { walk(it, i.second)  }
      .count()
  }

  println(silver())
  println(gold())
}
