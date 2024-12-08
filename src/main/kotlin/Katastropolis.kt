package io.github.jangalinski.kata

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter
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

fun main() {

  val md = FlexmarkHtmlConverter.builder()
    .build().convert("""
    <h2>--- Day 1: Historian Hysteria ---</h2>
    <p>The <em>Chief Historian</em> is always present for the big Christmas sleigh launch, but nobody has seen him in months! Last anyone heard, he was visiting locations that are historically significant to the North Pole; a group of Senior Historians has asked you to accompany them as they check the places they think he was most likely to visit.</p>
    <p>As each location is checked, they will mark it on their list with a <em class="star">star</em>. They figure the Chief Historian <em>must</em> be in one of the first fifty places they'll look, so in order to save Christmas, you need to help them get <em class="star">fifty stars</em> on their list before Santa takes off on December 25th.</p>
    <p>Collect stars by solving puzzles. Two puzzles will be made available on each day in the Advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants <em class="star">one star</em>. Good luck!</p>
    <p>You haven't even left yet and the group of Elvish Senior Historians has already hit a problem: their list of locations to check is currently <em>empty</em>. Eventually, someone decides that the best place to check first would be the Chief Historian's office.</p>
    <p>Upon pouring into the office, everyone confirms that the Chief Historian is indeed nowhere to be found. Instead, the Elves discover an assortment of notes and lists of historically significant locations! This seems to be the planning the Chief Historian was doing before he left. Perhaps these notes can be used to determine which locations to search?</p>
    <p>Throughout the Chief's office, the historically significant locations are listed not by name but by a unique number called the <em>location ID</em>. To make sure they don't miss anything, The Historians split into two groups, each searching the office and trying to create their own complete list of location IDs.</p>
    <p>There's just one problem: by holding the two lists up <em>side by side</em> (your puzzle input), it quickly becomes clear that the lists aren't very similar. Maybe you can help The Historians reconcile their lists?</p>
    <p>For example:</p>
    <pre><code>3   4
    4   3
    2   5
    1   3
    3   9
    3   3
    </code></pre>
    <p>Maybe the lists are only off by a small amount! To find out, pair up the numbers and measure how far apart they are. Pair up the <em>smallest number in the left list</em> with the <em>smallest number in the right list</em>, then the <em>second-smallest left number</em> with the <em>second-smallest right number</em>, and so on.</p>
    <p>Within each pair, figure out <em>how far apart</em> the two numbers are; you'll need to <em>add up all of those distances</em>. For example, if you pair up a <code>3</code> from the left list with a <code>7</code> from the right list, the distance apart is <code>4</code>; if you pair up a <code>9</code> with a <code>3</code>, the distance apart is <code>6</code>.</p>
    <p>In the example list above, the pairs and distances would be as follows:</p>
    <ul>
     <li>The smallest number in the left list is <code>1</code>, and the smallest number in the right list is <code>3</code>. The distance between them is <code><em>2</em></code>.</li>
     <li>The second-smallest number in the left list is <code>2</code>, and the second-smallest number in the right list is another <code>3</code>. The distance between them is <code><em>1</em></code>.</li>
     <li>The third-smallest number in both lists is <code>3</code>, so the distance between them is <code><em>0</em></code>.</li>
     <li>The next numbers to pair up are <code>3</code> and <code>4</code>, a distance of <code><em>1</em></code>.</li>
     <li>The fifth-smallest numbers in each list are <code>3</code> and <code>5</code>, a distance of <code><em>2</em></code>.</li>
     <li>Finally, the largest number in the left list is <code>4</code>, while the largest number in the right list is <code>9</code>; these are a distance <code><em>5</em></code> apart.</li>
    </ul>
    <p>To find the <em>total distance</em> between the left list and the right list, add up the distances between all of the pairs you found. In the example above, this is <code>2 + 1 + 0 + 1 + 2 + 5</code>, a total distance of <code><em>11</em></code>!</p>
    <p>Your actual left and right lists contain many location IDs. <em>What is the total distance between your lists?</em></p>
  """.trimIndent());

  println(md)
}

typealias Chrid=Krid<Char>
