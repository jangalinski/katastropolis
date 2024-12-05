package io.github.jangalinski.kata.adventOfCode

import io.github.jangalinski.kata.adventOfCode.AoCUtil.ListExt.peekPrint
import io.github.jangalinski.kata.adventOfCode.AoCUtil.ListExt.permutations
import io.github.jangalinski.kata.adventOfCode.AoCUtil.StringExt.chunkedByEmpty

object AoCUtil {

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

    fun String.intValues() = this.split("""[\s+, ]""".toRegex()).map { it.trim() }.filterNot { it.isBlank() }.map { it.toInt() }
    fun String.longValues() = this.split("""[, ]""".toRegex()).map { it.trim() }.filterNot { it.isBlank() }.map { it.toLong() }
    fun String.doubleValues() = this.split("""[, ]""".toRegex()).map { it.trim() }.filterNot { it.isBlank() }.map { it.toDouble() }
  }

  class Input(private val resource: String) {
    constructor(year: Int, day: Int, part: Int = 1, test: Boolean = false) : this(
      "adventOfCode/_$year/${
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

    fun <T> Sequence<T>.peekPrint() = map {
      println(it)
      it
    }

    fun <T> Iterable<T>.peekPrint() = peek { println(it) }
    fun <T> Iterable<T>.peek(fn: (T) -> Unit) = map {
      fn(it)
      it
    }

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
}

fun main(args: Array<String>) {
  val l = listOf(0 to 1, 2 to 3, 3 to 4)
  val p = l.permutations()
  p.peekPrint()
}
