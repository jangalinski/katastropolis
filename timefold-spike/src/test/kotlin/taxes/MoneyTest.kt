package io.github.jangalinski.spike.timefold.taxes

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.math.BigDecimal

class MoneyTest {

  @ParameterizedTest
  @CsvSource(
    value = [
      "PCT_07, TaxRate(7.00 %)",
      "PCT_19, TaxRate(19.00 %)",
    ]
  )
  fun `taxrate to String`(taxRate: TaxRate, expected: String) {
    assertThat(taxRate.toString()).isEqualTo(expected)
  }

  @Test
  fun `net amount prints rounded to 2 decimals`() {
    val net = NetAmount(BigDecimal("16.12345"))
    assertEquals("NetAmount(16.12)", net.toString())
  }

  @Test
  fun `gross and tax from net at 19 percent`() {
    val rate = TaxRate.PCT_19
    val net = NetAmount(BigDecimal("100"))

    val tax = net.taxAt(rate)
    val gross = net.grossAt(rate)

    assertEquals("TaxAmount(19.00)", tax.toString())
    assertEquals("GrossAmount(119.00)", gross.toString())
  }

  @Test
  fun `net from gross at 7 percent`() {
    val rate = TaxRate.PCT_07
    val gross = GrossAmount(BigDecimal("107"))

    val net = gross.netAt(rate)
    val tax = gross.taxAt(rate)

    assertEquals("NetAmount(100.00)", net.toString())
    assertEquals("TaxAmount(7.00)", tax.toString())
  }
}
