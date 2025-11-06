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
  val values: MutableList<NetAmountWithTax> = mutableListOf(),
  @ProblemFactProperty
  val total : Total = Total(TaxAmount(BigDecimal.ZERO), GrossAmount(BigDecimal.ZERO))
) : MutableList<NetAmountWithTax> by values {

  data class Total(
    val totalTax: TaxAmount,
    val totalGross: GrossAmount,
  ) {
    val totalNet = totalGross - totalTax
  }

  @ValueRangeProvider
  val taxRates = listOf(TaxRate.PCT_07, TaxRate.PCT_19)

  @PlanningScore
  lateinit var score: HardSoftScore

  val sumNetAmount: NetAmount get() = NetAmount(values.sumOf { it.netAmount.amount })
  val sumTaxAmount: TaxAmount get() = TaxAmount(values.sumOf { it.taxAmount.amount })
  val sumGrossAmount: GrossAmount get() = GrossAmount(values.sumOf { it.grossAmount.amount })
  override fun toString(): String {
    return "NetAmountWithTaxList(total=$total, values=$values, score=$score, sumNetAmount=$sumNetAmount, sumTaxAmount=$sumTaxAmount, sumGrossAmount=$sumGrossAmount, taxRates=$taxRates)"
  }


}
