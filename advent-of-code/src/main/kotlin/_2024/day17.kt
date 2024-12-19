package io.github.jangalinski.kata.aoc._2024

import io.github.jangalinski.kata.aoc.pow


fun main() {
  //val computers = listOf(
//    If register C contains 9, the program 2,6 would set register B to 1.
//    If register A contains 10, the program 5,0,5,1,5,4 would output 0,1,2.
//    If register A contains 2024, the program 0,1,5,4,3,0 would output 4,2,5,6,7,7,7,7,3,1,0 and leave 0 in register A.
//      If register B contains 29, the program 1,7 would set register B to 26.
//    If register B contains 2024 and register C contains 43690, the program 4,0 would set register B to 44354.
//  )

  fun eval(program: List<Int>, registers: Triple<Int, Int, Int>): List<Int> {
    var pointer = 0
    var registerA = registers.first
    var registerB = registers.second
    var registerC = registers.third

    fun get(): Triple<Int, Int, Int> {
      val instruction = program[pointer]
      val literal = program[pointer + 1]
      val ccombo = when (literal) {
        0 -> 0
        1 -> 1
        2 -> 2
        3 -> 3
        4 -> registerA
        5 -> registerB
        6 -> registerC
        7 -> TODO("reserved")
        else -> TODO("forbidden")
      }
      return Triple(instruction, literal, ccombo)
    }

    return sequence {
      while (pointer < program.size) {
        val (instruction, literal, combo) = get()

        when (instruction) {
          0 -> {
            registerA = registerA / (2.pow(combo))
            pointer += 2
          }

          1 -> {
            registerB = registerB xor literal
            pointer += 2
          }

          2 -> {
            registerB = combo % 8
            pointer += 2
          }

          3 -> {
            if (registerA == 0) {
              pointer += 2
            } else {
              pointer = literal
            }
          }

          4 -> {
            registerB = registerB xor registerC
            pointer += 2
          }

          5 -> {
            yield(combo % 8)
            pointer += 2
          }

          6 -> {
            registerB = registerA / (2.pow(combo))
            pointer += 2
          }

          7 -> {
            registerC = registerA / 2.pow(combo)
            pointer += 2
          }

          else -> TODO("undefined")
        }
      }
    }.toList()
  }

//  fun read(test: Boolean): Computer {
//    return if (test) {
//      Computer(program = listOf(0, 1, 5, 4, 3, 0), A = 729, B = 0, C = 0)
//    } else {
//      Computer(program = listOf(2, 4, 1, 5, 7, 5, 0, 3, 4, 1, 1, 6, 5, 5, 3, 0), A = 47719761, B = 0, C = 0)
//    }
//  }
//
//  val computer = read(true)
////  println(computer)
//
//
//  //print(silver(computer).output)
//
////  println(Computer(program = listOf(2, 6), C=9))
////
////
////  println(Computer(program = listOf(2, 6), C=9)())
//
//
////  If register C contains 9, the program 2,6 would set register B to 1.
//
////  If register A contains 2024, the program 0,1,5,4,3,0 would output 4,2,5,6,7,7,7,7,3,1,0 and leave 0 in register A.
////  If register B contains 29, the program 1,7 would set register B to 26.
////  If register B contains 2024 and register C contains 43690, the program 4,0 would set register B to 44354.
////
//
////  If register A contains 10, the program 5,0,5,1,5,4 would output 0,1,2.
//  println(Computer(A=2024, program = listOf(0,1,5,4,3,0)))
// println(silver(Computer(A=2024, program = listOf(0,1,5,4,3,0)))?.output)

  val test = listOf(0, 1, 5, 4, 3, 0) to Triple(729, 0, 0)
  val prod =  listOf(2, 4, 1, 5, 7, 5, 0, 3, 4, 1, 1, 6, 5, 5, 3, 0) to Triple(47719761,  0,  0)

  //println(eval(prod.first, prod.second).joinToString(separator = ","))
  //4,6,3,5,6,3,5,2,1,0.

  fun gold(p: List<Int>) : Int{
    val s = generateSequence(535527000) { it+1 }.map {
      //if (it % 1000 == 0) print("$it .... ")
      it to eval( p, Triple(it,0,0))
    }
    return s.filter { it.second == p }.first().first
  }


  println(gold(prod.first))
}
