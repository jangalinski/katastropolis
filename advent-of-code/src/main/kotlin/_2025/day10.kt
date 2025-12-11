package io.github.jangalinski.kata.aoc._2025

import io.github.jangalinski.kata.aoc.AocUtil
import io.github.jangalinski.kata.aoc.AocUtil.ListExt.head

data object Day10 {
  fun read(test:Boolean=false): List<Pair<List<Pair<Boolean, Int>>, List<List<Int>>>> = AocUtil.Input(year = 2025, day = 10, part = 1, test = test)
    .nonEmptyLines
    .map { it.trim().replace("""[\[\](){}]""".toRegex(),"").split(" ") }
    .map { line ->
      val (a, b) = line.head()
      val (c,d) = b.reversed().head()

      val lights = a.map { it == '#' }
      val joltages = c.split(",").map { it.toInt() }

      lights.zip(joltages) to ( d.reversed().map { it.split(",").map { it.toInt()} })
    }

  data class LightsSilver(val lights: List<Boolean>) {
    constructor(size:Int) : this(List(size) { false })

    val size = lights.size

    operator fun plus(button : Button) = copy(lights = this.lights.mapIndexed { i, b ->
      if (button.contains(i)) !b else b
    })

  }

  @Suppress("JavaDefaultMethodsNotOverriddenByDelegation")
  @JvmInline
  value class Button(val indexes: List<Int> = emptyList()): List<Int> by indexes

  fun silverInput(test: Boolean = false) = read(test).map { (lights, buttons) ->
    LightsSilver(lights.map { it.first }) to buttons.map { Button(it) }
  }
}

fun main() {
  val input = Day10.silverInput()

  fun silver(input: List<Pair<Day10.LightsSilver, List<Day10.Button>>>) : Int {
    TODO()
  }


  input.forEach { println(it)}

  input.maxBy { it.second.size }.let { println(it.second.size) }

}
