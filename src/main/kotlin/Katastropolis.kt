package io.github.jangalinski.kata

import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.fn.IndexTransformer
import io.toolisticon.lib.krid.model.*
import io.toolisticon.lib.krid.model.step.Direction
import io.toolisticon.lib.krid.model.step.Direction.DOWN
import io.toolisticon.lib.krid.model.step.Direction.DOWN_LEFT
import io.toolisticon.lib.krid.model.step.Direction.DOWN_RIGHT
import io.toolisticon.lib.krid.model.step.Direction.LEFT
import io.toolisticon.lib.krid.model.step.Direction.NONE
import io.toolisticon.lib.krid.model.step.Direction.RIGHT
import io.toolisticon.lib.krid.model.step.Direction.UP
import io.toolisticon.lib.krid.model.step.Direction.UP_LEFT
import io.toolisticon.lib.krid.model.step.Direction.UP_RIGHT

object Katastropolis {

  fun <T> List<T>.head(): Pair<T, List<T>> = toMutableList().let {
    val head = it.removeFirst()
    head to it.toList()
  }

  inline fun <T> Iterable<T>.peek(crossinline action: (T) -> Unit): List<T> {
    return map {
      action(it)
      it
    }
  }

  fun <T> Iterable<T>.println(): List<T> = peek(::println)


  fun Int.even() = this % 2 == 0
  fun Int.odd() = !even()


  data class PrimeBase(
    val map: Map<Int, Int>
  ) {
    companion object {

      operator fun invoke(): PrimeBase {
        return PrimeBase(
          mapOf(
            1 to 0,
            2 to 0,
            3 to 0,
            5 to 0,
            7 to 0,
            11 to 0,
            13 to 0,
            17 to 0,
            19 to 0,
            23 to 0,
            29 to 0,
            31 to 0,
            37 to 0,
            41 to 0,
            43 to 0,
            47 to 0,
            53 to 0,
            59 to 0,
            61 to 0,
            67 to 0,
            71 to 0,
            73 to 0,
            79 to 0,
            83 to 0,
            89 to 0,
            97 to 0,
          )
        )
      }

      operator fun invoke(num: Int): PrimeBase {
        return PrimeBase(mapOf(1 to 0))
      }
    }

    val longValue by lazy {
      map.entries.fold(0L) { s, (k, v) ->
        s + k * v
      }
    }
  }

  fun factors(n: Int) {
    if (n < 1) return
    (1..n / 2)
      .filter { n % it == 0 }
      .forEach { print("$it ") }
  }

  fun printFactors(n: Int) {
    if (n < 1) return
    print("$n => ")
    (1..n / 2)
      .filter { n % it == 0 }
      .forEach { print("$it ") }
    println(n)
  }

  object KridExt {

    fun <E : Any> Row<E>.cellValues(): List<CellValue<E>> = values.mapIndexed { x, v -> CellValue<E>(x, index, v) }
    fun <E : Any> Column<E>.cellValues(): List<CellValue<E>> = values.mapIndexed { y, v -> CellValue<E>(index, y, v) }

    fun <E : Any> krid(dimension: Dimension, emptyValue: E, cellValues: List<CellValue<E>>): Krid<E> {
      return Krids.krid(
        dimension = dimension,
        emptyElement = emptyValue,
        initialize = { x, y ->
          cellValues.first { it.x == x && it.y == y }.value
        }
      )
    }


    fun <E> krid(columns: Columns<E>, emptyElement: E): Krid<E> {
      require(columns.isNotEmpty()) { "columns must not be empty: $columns" }
      require(columns.none { it.isEmpty() }) { "no columns must be empty: $columns" }
      require(columns.maxOf { it.size } == columns.minOf { it.size }) { "all columns must have same size: $columns" }

      val dimension = Dimension(width = columns.size, height = columns[0].size)

      val list = buildList<E> {
        columns.sortedBy { it.index }.forEach { col ->
          dimension.rowRange.forEach { index ->
            add(col[index])
          }
        }
      }

      return Krid(
        dimension = dimension,
        emptyElement = emptyElement,
        list = list
      )
    }

    val Krid<*>.indexTransformer: IndexTransformer get() = IndexTransformer(this.width)

//fun <E:Any> krid(krid : Krid<E>,)

    infix fun Cell.inSameRow(b: Cell) = y == b.y
    infix fun Cell.inSameColumn(b: Cell) = x == b.x

    infix fun Cell.isAdjacent(other: Cell) = adjacent.contains(other)
    infix fun Cell.isAdjacentOrEqual(other: Cell) = this == other || adjacent.contains(other)

    /**
     * Only combine UP/DOWN with LEFT/RIGHT (and NONE)
     */
    infix fun Direction.combine(other: Direction): Direction = when (this) {
      NONE -> other
      UP -> when (other) {
        RIGHT -> UP_RIGHT
        LEFT -> UP_LEFT
        NONE -> this
        else -> throw IllegalArgumentException("$this can only be combined with LEFT/RIGHT/NONE.")
      }

      DOWN -> when (other) {
        LEFT -> DOWN_LEFT
        RIGHT -> DOWN_RIGHT
        NONE -> this
        else -> throw IllegalArgumentException("$this can only be combined with LEFT/RIGHT/NONE.")
      }

      RIGHT -> when (other) {
        UP -> UP_RIGHT
        DOWN -> DOWN_RIGHT
        NONE -> this
        else -> throw IllegalArgumentException("$this can only be combined with UP/DOWN/NONE.")
      }

      LEFT -> when (other) {
        UP -> UP_LEFT
        DOWN -> DOWN_LEFT
        NONE -> this
        else -> throw IllegalArgumentException("$this can only be combined with UP/DOWN/NONE.")
      }

      else -> throw IllegalArgumentException("combine only works with vertical + horizontal, no mix allowed.")
    }

    fun Cell.beam(direction: Direction, includeStart: Boolean = false) = direction.beam(this, includeStart)
    fun Cell.take(direction: Direction, num: Int, includeStart: Boolean = false) = beam(direction, includeStart).take(num).toList()

    fun Direction.turn(direction: Direction) : Direction{
      require(direction in setOf(LEFT, RIGHT)) {"Only Left/Right is allowed: $direction"}
      return when (this) {
        UP -> if (RIGHT == direction) RIGHT else LEFT
        UP_RIGHT -> TODO()
        RIGHT -> if (RIGHT == direction) DOWN else UP
        DOWN_RIGHT -> TODO()
        DOWN -> if (RIGHT == direction) LEFT else RIGHT
        DOWN_LEFT -> TODO()
        LEFT -> if (RIGHT == direction) UP else DOWN
        UP_LEFT -> TODO()
        NONE -> NONE
      }
    }
  }
}
