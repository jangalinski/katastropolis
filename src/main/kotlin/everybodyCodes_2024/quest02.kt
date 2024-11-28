package io.github.jangalinski.kata.everybodyCodes_2024

import java.util.regex.Pattern
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

fun main() {

  val lines = q2.lines()

  val words = lines[0].removePrefix("WORDS:").split(",").map{it.trim()}.filter{it.isNotEmpty()}.toSet()

  val m = Pattern.compile(words.joinToString("|")).matcher(lines[2])
  var c = 0
  while (m.find()) {
    c++
  }
  println(c)

}




private val q2t = """
  WORDS:THE,OWE,MES,ROD,HER

  AWAKEN THE POWER ADORNED WITH THE FLAMES BRIGHT IRE
  4
  THE FLAME SHIELDED THE HEART OF THE KINGS
  3
  POWE PO WER P OWE R
  2
  THERE IS THE END
  3
""".trimIndent()

private val q2 = """
  WORDS:LOR,LL,SI,OR,UI,M_,AT

  LOREM IPSUM DOLOR SIT AMET, CONSECTETUR ADIPISCING ELIT, SED DO EIUSMOD TEMPOR INCIDIDUNT UT LABORE ET DOLORE MAGNA ALIQUA_ UT ENIM AD MINIM VENIAM, QUIS NOSTRUD EXERCITATION ULLAMCO LABORIS NISI UT ALIQUIP EX EA COMMODO CONSEQUAT_ DUIS AUTE IRURE DOLOR IN REPREHENDERIT IN VOLUPTATE VELIT ESSE CILLUM DOLORE EU FUGIAT NULLA PARIATUR_ EXCEPTEUR SINT OCCAECAT CUPIDATAT NON PROIDENT, SUNT IN CULPA QUI OFFICIA DESERUNT MOLLIT ANIM ID EST LABORUM_
""".trimIndent()
