package io.github.jangalinski.kata.aoc._2024

import io.github.jangalinski.kata.aoc.AocUtil
import io.github.jangalinski.kata.aoc.Chrid
import io.toolisticon.lib.krid.Krids.krid
import jdk.internal.vm.vector.VectorSupport.test

fun main() {
  fun read(test:Boolean): Pair<List<List<Int>>,List<List<Int>>> {
    val krids =  AocUtil.Input(year = 2024, day = 25, test = test).contentRaw.split("\n\n")
      .map { krid(it, '.') }.map { (it.getValue(0,0).value == '#') to (it.columns().map { it.count { it == '#' } -1 }) }
      .groupBy({it.first},{it.second})


    return krids.getOrDefault(true, emptyList()) to krids.getOrDefault(false, emptyList())
  }


  fun silver(input :  Pair<List<List<Int>>,List<List<Int>>>) : Int {
    val (locks, keys) = input

    val s = sequence {
      locks.forEach { l ->
        keys.forEach { k ->
          yield(l.zip(k))
        }
      }
    }

    return s.count { it.none { it.first + it.second > 5 }  }
  }


  println(silver(read(false)))

}
