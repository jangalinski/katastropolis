package io.github.jangalinski.kata.lib

import io.github.jangalinski.kata.advent_of_code.AoCUtil.ListExt.peekPrint

fun <T : Comparable<T>> Iterable<T>.permutations(): Sequence<Pair<T, T>> = sequence {
  val elements = this@permutations.toList()
  elements.forEach { a ->
    elements.filterNot { it == a }
      .map { a to it }
      .forEach { b ->
        yield(b)
      }
  }
}.map { it.toList().sorted() }
  .distinct()
  .map { val (a, b) = it; a to b }
