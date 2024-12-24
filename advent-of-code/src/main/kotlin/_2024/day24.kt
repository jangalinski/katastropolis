package io.github.jangalinski.kata.aoc._2024

import io.github.jangalinski.kata.aoc.AocUtil
import io.github.jangalinski.kata.aoc.AocUtil.StringExt.chunkedByEmpty
import java.math.BigInteger

private enum class OP : (Boolean, Boolean) -> Boolean {
  AND {
    override fun invoke(p1: Boolean, p2: Boolean): Boolean = p1 and p2
  },
  OR {
    override fun invoke(p1: Boolean, p2: Boolean): Boolean = p1 or p2
  },
  XOR {
    override fun invoke(p1: Boolean, p2: Boolean): Boolean = p1 xor p2
  };
}

fun main() {

  data class Line(val a: String, val b: String, val r: String, val op: OP) {
    val wires = setOf<String>(a,b,r)
    fun isSolved(result:  Map<String, Boolean?>) = result[a] != null && result[b] != null && result[r] != null

    fun isSolvable(result:  Map<String, Boolean?>): Boolean = result[a] != null && result[b] != null && result[r] == null

    fun solve(result:  Map<String, Boolean?>) : Pair<String,Boolean> {
      val va = requireNotNull(result[a])
      val vb = requireNotNull(result[b])
      return r to op(va,vb)
    }
  }

  fun read(n: Int): Pair<Map<String, Boolean?>, List<Line>> {
    val (startValues, wiring) = when (n) {
      0 -> """
        x00: 1
        x01: 1
        x02: 1
        y00: 0
        y01: 1
        y02: 0

        x00 AND y00 -> z00
        x01 XOR y01 -> z01
        x02 OR y02 -> z02
      """.trimIndent()

      1 -> """
        x00: 1
        x01: 0
        x02: 1
        x03: 1
        x04: 0
        y00: 1
        y01: 1
        y02: 1
        y03: 1
        y04: 1

        ntg XOR fgs -> mjb
        y02 OR x01 -> tnw
        kwq OR kpj -> z05
        x00 OR x03 -> fst
        tgd XOR rvg -> z01
        vdt OR tnw -> bfw
        bfw AND frj -> z10
        ffh OR nrd -> bqk
        y00 AND y03 -> djm
        y03 OR y00 -> psh
        bqk OR frj -> z08
        tnw OR fst -> frj
        gnj AND tgd -> z11
        bfw XOR mjb -> z00
        x03 OR x00 -> vdt
        gnj AND wpb -> z02
        x04 AND y00 -> kjc
        djm OR pbm -> qhw
        nrd AND vdt -> hwm
        kjc AND fst -> rvg
        y04 OR y02 -> fgs
        y01 AND x02 -> pbm
        ntg OR kjc -> kwq
        psh XOR fgs -> tgd
        qhw XOR tgd -> z09
        pbm OR djm -> kpj
        x03 XOR y03 -> ffh
        x00 XOR y04 -> ntg
        bfw OR bqk -> z06
        nrd XOR fgs -> wpb
        frj XOR qhw -> z04
        bqk OR frj -> z07
        y03 OR x01 -> nrd
        hwm AND bqk -> z03
        tgd XOR rvg -> z12
        tnw OR pbm -> gnj
      """.trimIndent()

      else -> AocUtil.Input(year = 2024, day = 24).contentRaw
    }.chunkedByEmpty()

    val w = wiring.map {
      val (a, op, b, _, o) = it.split(" "); Line(
      a, b, o, OP.valueOf(op))
    }
    val map = buildMap<String,Boolean?> {
      startValues.map { val (s, b) = it.trim().split(": "); s to if (b == "1") true else false }
        .forEach { put(it.first, it.second) }

      w.flatMap { it.wires }.distinct().forEach {
        putIfAbsent(it, null)
      }
    }

    return map to w
  }

  val input = read(0)

  fun silver(input : Pair<Map<String, Boolean?>, List<Line>>) : BigInteger {
    val result = input.first.toMutableMap()
    val lines = input.second.toMutableList()


    while (lines.isNotEmpty()) {
      lines.removeIf { it.isSolved(result) }

      val l = lines.first { it.isSolvable(result) }.apply {
        lines.remove(this)
      }
      val (r, b) = l.solve(result)
      result[r] = b
    }

    val bs =  result.entries.filter { it.key.startsWith("z") }.sortedByDescending { it.key }
      .map { it.value!! }

    val r = bs
      .joinToString(separator = "") {
        if (it) "1" else "0"
      }


    return BigInteger(r, 2)
  }

  println(silver(read(0)))
  println(silver(read(1)))
  println(silver(read(2)))

}
