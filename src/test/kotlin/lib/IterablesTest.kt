package io.github.jangalinski.kata.lib

import io.toolisticon.lib.krid.Krids.cell
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled
class IterablesTest {

  @Test
  fun permutations() {
    val p = listOf(1,2,3).permutations().toList()
    assertThat(p).containsExactly(1 to 2, 1 to 3, 2 to 3)
  }

  @Test
  fun `sorted cells`() {
    val p = listOf(cell(5,2), cell(8,1)).permutations().toList()
    assertThat(p).hasSize(1)

    println(p)
  }
}
