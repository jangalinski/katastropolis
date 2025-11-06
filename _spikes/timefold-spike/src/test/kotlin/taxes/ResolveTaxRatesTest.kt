package io.github.jangalinski.spike.timefold.taxes

import ai.timefold.solver.core.api.solver.SolverFactory
import ai.timefold.solver.core.config.solver.SolverConfig
import ai.timefold.solver.core.config.solver.termination.TerminationConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.platform.commons.logging.LoggerFactory
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class ResolveTaxRatesTest {
  val logger = LoggerFactory.getLogger(ResolveTaxRatesTest::class.java)

  @Test
  fun `resolve for taxrates`() {
    val factory = SolverFactory.create<NetAmountWithTaxList>(
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

    val result = factory.buildSolver()
      .solve(
        NetAmountWithTaxList(
          values = mutableListOf(
            NetAmountWithTax(NetAmount(100.toBigDecimal())),
          ),
          total = NetAmountWithTaxList.Total(
            TaxAmount(19.toBigDecimal()), GrossAmount(119.toBigDecimal())
          )
        )
      )
    logger.info { result.toString() }

    assertThat(result.score.hardScore()).isEqualTo(0)
    assertThat(result.values.single().taxRate).isEqualTo(TaxRate.PCT_19)
  }
}
