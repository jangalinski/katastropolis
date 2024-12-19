package io.github.jangalinski.kata.aoc

import kotlin.math.pow

fun Int.pow(n: Int): Int = this.toDouble().pow(n).toInt()
