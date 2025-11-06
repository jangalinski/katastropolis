@file:Suppress("JavaDefaultMethodsNotOverriddenByDelegation")

package io.github.jangalinski.spike.timefold.taxes

import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty
import ai.timefold.solver.core.api.domain.solution.PlanningScore
import ai.timefold.solver.core.api.domain.solution.PlanningSolution
import ai.timefold.solver.core.api.domain.solution.ProblemFactProperty
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore
import java.math.BigDecimal

@PlanningSolution
class NetAmountWithTaxList @JvmOverloads constructor(
  @PlanningEntityCollectionProperty
  val values: List<NetAmountWithTax> = listOf(),
  @ProblemFactProperty
  val total : Total = Total(TaxAmount(BigDecimal.ZERO), GrossAmount(BigDecimal.ZERO))
) : List<NetAmountWithTax> by values {

  data class Total(
    val totalTax: TaxAmount,
    val totalGross: GrossAmount,
  ) {
    val totalNet = totalGross - totalTax
  }

  @ValueRangeProvider
  val taxRates = listOf(TaxRate.PCT_07, TaxRate.PCT_19)

  @PlanningScore
  var score: HardSoftScore = HardSoftScore.ZERO

  val sumNetAmount: NetAmount get() = NetAmount(values.sumOf { it.netAmount.amount })
  val sumTaxAmount: TaxAmount get() = TaxAmount(values.sumOf { it.taxAmount.amount })
  val sumGrossAmount: GrossAmount get() = GrossAmount(values.sumOf { it.grossAmount.amount })
  override fun toString(): String {
    // Avoid touching derived amounts here because entities may not be fully initialized when toString() is called by the solver/setup.
    return "NetAmountWithTaxList(total=$total, valuesCount=${values.size}, score=$score, taxRates=$taxRates)"
  }


}
