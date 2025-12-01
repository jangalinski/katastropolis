package io.github.jangalinski.kata.aoc

import io.github.jangalinski.kata.aoc.AocUtil.StringExt.chunkedByEmpty
import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.fn.IndexTransformer
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.CellValue
import io.toolisticon.lib.krid.model.Column
import io.toolisticon.lib.krid.model.Columns
import io.toolisticon.lib.krid.model.Dimension
import io.toolisticon.lib.krid.model.Row
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


object AocUtil {

  operator fun MatchResult.get(name: String): Long = this.groups[name]?.value?.toLong() ?: 0L

  object StringExt {
    fun String.nonEmptyLines() = lines().filterNot(String::isEmpty)

    fun String.chunkedByEmpty(): List<List<String>> = this.split("\n\n").map { it.nonEmptyLines() }

    fun String.toPair(splitter: String): Pair<String, String> {
      val (a, b) = this.split(splitter)
      return a.trim() to b.trim()
    }

    fun String.toIntPair(splitter: String): Pair<Int, Int> {
      val p = this.toPair(splitter)
      return p.first.toInt() to p.second.toInt()
    }

    fun String.splitTrimmed(splitter: String): List<String> = this.split(splitter).map { it.trim() }.filterNot(String::isEmpty)

    fun String.intValues() = this.split("""[\s+,: ]""".toRegex()).map { it.trim() }.filterNot { it.isBlank() }.map { it.toInt() }
    fun String.longValues() = this.split("""[, ]""".toRegex()).map { it.trim() }.filterNot { it.isBlank() }.map { it.toLong() }
    fun String.doubleValues() = this.split("""[, ]""".toRegex()).map { it.trim() }.filterNot { it.isBlank() }.map { it.toDouble() }
  }

  class Input(private val resource: String) {
    constructor(year: Int, day: Int, part: Int = 1, test: Boolean = false) : this(
      "_$year/${
        day.toString().padStart(2, '0')
      }-$part${if (test) "-test" else ""}.txt"
    )

    val contentRaw by lazy {
      requireNotNull(Input::class.java.getResource("/$resource")).readText()
    }

    val contentTrimmed = contentRaw.lines().joinToString("\n") { it.trim() }

    val nonEmptyLines by lazy {
      contentTrimmed.lines().filterNot { it.isEmpty() }
    }

    override fun toString() = "Input(file='$resource', contentRaw='$contentRaw')"

    fun linesChunkedByEmpty() = contentTrimmed.chunkedByEmpty()
  }

  object ListExt {

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

    fun <T> Sequence<T>.peekPrint() = map {
      println(it)
      it
    }

    fun <T> Iterable<T>.peekPrint() = peek { println(it) }

    fun <T> Collection<T>.allPairs(filterBidirex: Boolean = false): List<Pair<T, T>> = buildList {
      this@allPairs.forEach { f ->
        this@allPairs.forEach { s ->
          if (f != s) {
            if (filterBidirex) {
              if (!this.contains(s to f)) {
                this.add(f to s)
              }
            } else {
              this.add(f to s)
            }
          }
        }
      }
    }

    fun <T> List<T>.permutations(): Set<List<T>> {
      if (this.isEmpty()) return setOf(emptyList())

      val result: MutableSet<List<T>> = mutableSetOf()
      for (i in this.indices) {
        val list = this - this[i]
        list.permutations().forEach { item ->
          result.add(item + this[i])
        }
      }
      return result
    }
  }

  fun String.toNonEmptyTrimmedLines() = this.lines()
    .map { it.trim() }
    .filterNot { it.isEmpty() }

  fun String.toNonEmptyTrimmed(separator: String = "\n") = toNonEmptyTrimmedLines().joinToString(separator = separator)


  fun Long.factorsSequence(): Sequence<Long> {
    val n = this
    return sequence {
      (1..n / 2).forEach {
        if (n % it == 0L) yield(it)
      }
      yield(n)
    }
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

typealias Chrid=Krid<Char>
typealias IntKrid=Krid<Int>

typealias IntPair = Pair<Int, Int>
typealias LongPair = Pair<Long, Long>
