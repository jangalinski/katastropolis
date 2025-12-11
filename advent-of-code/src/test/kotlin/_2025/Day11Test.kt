package io.github.jangalinski.kata.aoc._2025

import io.github.jangalinski.kata.aoc.AocUtil
import io.github.jangalinski.kata.aoc.AocUtil.ListExt.head
import org.jgrapht.Graph
import org.jgrapht.alg.shortestpath.AllDirectedPaths
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import org.junit.jupiter.api.Test

class Day11Test {

  @Test
  fun `solve gold`() {

    fun read(test: Boolean = false, second : Boolean = false) = AocUtil.Input(year = 2025, day = 11, part = if (second) 2 else 1, test = test).nonEmptyLines
        .map {
          it.split(":", " ").map(String::trim).filter(String::isNotBlank).head()
        }.fold(DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge::class.java)) { graph, (s,t) ->
          graph.apply {
            addVertex(s)
            t.forEach {
              addVertex(it)
              addEdge(s, it)
            }
          }
        }

      val test = false
      val input = read(test, false)

      // silver
      //println(AllDirectedPaths(input).getAllPaths("you", "out", true, null).size)

      //val allPath = AllDirectedPaths(input).getAllPaths("you", "out", true, null)

      // gold
//  println(
//    AllDirectedPaths(input).getAllPaths("svr", "out", true, null)
//      .map { it.vertexList.toSet() }.count { it.containsAll(setOf("fft", "dac")) }
//  )

      try {
        //AllDirectedPaths(input).getAllPaths("svr", "out", false, 100).size.let { println(it) }

        fun countPaths(g: Graph<String, DefaultEdge>, a: String, b: String) =
          AllDirectedPaths(g).getAllPaths(a, b, true, null).size
        val start = "svr"
        val end = "out"
        val stop1 = "fft"
        val stop2 = "dac"

        val sa = countPaths(input, start, stop1).toLong()
        println("sa = $sa")
        val ab = countPaths(input, stop1, stop2).toLong()
        println("ab = $ab")
        val be = countPaths(input, stop2, end).toLong()
        println("be = $be")

        val sb = countPaths(input, start, stop2).toLong()
        println("sb = $sb")
        val ba = countPaths(input, stop2, stop1).toLong()
        println("ba = $ba")
        val ae = countPaths(input, stop1, end).toLong()
        println("ae = $ae")

        val total = sa*ab*be + sb*ba*ae
        println("Total = $total")
      } catch (e: Exception) {
        println(e.message)
      }
      //AllDirectedPaths(input).getAllPaths("svr", "out", true, null).size.let { println(it) }

      //println (input)
  }
}
