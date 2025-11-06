package io.github.jangalinski.spike.timefold.taxes

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode


class NetAmountWithTaxTest {
  @Test
  fun `calculate tax for 0`() {
    val amount = NetAmountWithTax(NetAmount(10.toBigDecimal()))

    assertThat(amount.netAmount.amount).isEqualTo(10.toBigDecimal())
    assertThat(amount.grossAmount.amount).isEqualTo(10.toBigDecimal())
    assertThat(amount.taxAmount.amount).isEqualTo(BigDecimal.ZERO)
  }

  @Test
  fun `calculate tax for 19`() {
    val amount = NetAmountWithTax(NetAmount(100.toBigDecimal()))
    amount.taxRate = TaxRate.PCT_19

    assertThat(amount.netAmount.amount).isEqualTo(100.toBigDecimal())
    assertThat(amount.grossAmount.rounded()).isEqualTo(GrossAmount(119.toBigDecimal()).rounded())
    assertThat(amount.taxAmount.rounded()).isEqualTo(TaxAmount(19.toBigDecimal()).rounded())
  }
}
