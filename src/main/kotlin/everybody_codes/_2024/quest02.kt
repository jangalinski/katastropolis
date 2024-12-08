package io.github.jangalinski.kata.everybody_codes._2024

import java.util.regex.Pattern

fun main() {

  val lines = q2.lines()

  val words = lines[0].removePrefix("WORDS:").split(",").map{it.trim()}.filter{it.isNotEmpty()}
    .map { it.replace(".","\\.") }
    .toSet()

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
  WORDS:LOR,LL,SI,OR,UI,M.,AT

  LOREM IPSUM DOLOR SIT AMET, CONSECTETUR ADIPISCING ELIT, SED DO EIUSMOD TEMPOR INCIDIDUNT UT LABORE ET DOLORE MAGNA ALIQUA. UT ENIM AD MINIM VENIAM, QUIS NOSTRUD EXERCITATION ULLAMCO LABORIS NISI UT ALIQUIP EX EA COMMODO CONSEQUAT. DUIS AUTE IRURE DOLOR IN REPREHENDERIT IN VOLUPTATE VELIT ESSE CILLUM DOLORE EU FUGIAT NULLA PARIATUR. EXCEPTEUR SINT OCCAECAT CUPIDATAT NON PROIDENT, SUNT IN CULPA QUI OFFICIA DESERUNT MOLLIT ANIM ID EST LABORUM.
""".trimIndent()
