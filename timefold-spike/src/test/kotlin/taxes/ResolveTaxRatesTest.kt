package io.github.jangalinski.spike.timefold.taxes

import ai.timefold.solver.core.api.solver.SolverFactory
import ai.timefold.solver.core.config.solver.SolverConfig
import ai.timefold.solver.core.config.solver.termination.TerminationConfig
import io.github.oshai.kotlinlogging.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.converter.ArgumentConverter
import org.junit.jupiter.params.converter.ConvertWith
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.kotlin.mock
import java.math.BigDecimal
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

private val logger = KotlinLogging.logger {}

internal class BigDecimalListConverter : ArgumentConverter {
  companion object {
    // Matches integers or decimals (optional leading minus), e.g. 1, 1.0, -3.14
    val pattern = """-?\d+(?:\.\d+)?""".toRegex()
  }

  override fun convert(source: Any?, context: ParameterContext): List<BigDecimal> {
    require(source is String) { "source is not a String: $source" }
    val numbers = pattern.findAll(source)
      .map { it.value.toBigDecimal().setScale(2, java.math.RoundingMode.HALF_UP) }
      .toList()
    require(numbers.isNotEmpty()) { "no numeric values found in: $source" }
    return numbers
  }

}

class ResolveTaxRatesTest {

  fun solverFactory() = SolverFactory.create<NetAmountWithTaxList>(
    SolverConfig()
      .withSolutionClass(NetAmountWithTaxList::class.java)
      .withEntityClasses(NetAmountWithTax::class.java)
      .withConstraintProviderClass(TaxRateConstraintProvider::class.java)
      .withTerminationConfig(
        TerminationConfig().apply {
          bestScoreLimit = "0hard/*soft"
          spentLimit = 5.seconds.toJavaDuration()
        }
      )
  )

  @ParameterizedTest
  @CsvSource(
    value = [
      "true; 19; 119; [100]",
      "false; 19; 119; [10]",
      "true; 26.00; 226.00; [100.00, 100.00]",
      "true; 45.00; 345.00; [200.00, 100.00]",
    ], nullValues = ["null"], delimiterString = ";"
  )
  fun `resolve tax rates`(
    success: Boolean,
    taxAmount: BigDecimal,
    grossAmount: BigDecimal,
    @ConvertWith(BigDecimalListConverter::class) netAmounts: List<BigDecimal>
  ) {
    val factory = solverFactory()

    val result = factory.buildSolver()
      .solve(
        NetAmountWithTaxList(
          values = netAmounts.map { NetAmountWithTax(NetAmount(it)) },
          total = NetAmountWithTaxList.Total(
            totalTax = TaxAmount(taxAmount),
            totalGross = GrossAmount(grossAmount)
          )
        )
      )

    if (success) {
      assertThat(result.score.hardScore()).isEqualTo(0)
    } else {
      assertThat(result.score.hardScore()).isEqualTo(-1)
    }
    logger.info { result.values.toString() }
  }

  @Test
  fun `verify converter`() {
    val converter = BigDecimalListConverter()
    val ctx = mock<ParameterContext>{}

    val result = converter.convert("[    1.00, 2 ,   31.23]", ctx)

    assertThat(result).containsExactly("1.00".toBigDecimal(), "2.00".toBigDecimal(), "31.23".toBigDecimal())
  }
}
