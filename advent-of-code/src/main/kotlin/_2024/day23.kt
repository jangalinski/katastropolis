package io.github.jangalinski.kata.aoc._2024

import io.github.jangalinski.kata.aoc.AocUtil
import org.jgrapht.Graph
import org.jgrapht.alg.cycle.CycleDetector
import org.jgrapht.alg.cycle.SzwarcfiterLauerSimpleCycles
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleGraph

fun main() {

  val input = AocUtil.Input(year = 2024, day = 23, test = false).nonEmptyLines.map {
    val (x, y) = it.trim().split("-")
    x to y
  }


  val g = input.fold(DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge::class.java)) { a, c ->
    a.apply {
      addVertex(c.first)
      addVertex(c.second)

      addEdge(c.first, c.second)
      addEdge(c.second, c.first)
    }
  }

  fun silver(g: Graph<String, DefaultEdge>): Int {
    val c = SzwarcfiterLauerSimpleCycles(g).findSimpleCycles().filter { it.size == 3 }

    return c.map { it.sorted() }.distinct()
      .count { it.any { it.startsWith("t") } }

  }


  println(silver(g))
}
