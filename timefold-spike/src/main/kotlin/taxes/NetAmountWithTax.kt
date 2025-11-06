package io.github.jangalinski.spike.timefold.taxes

import ai.timefold.solver.core.api.domain.entity.PlanningEntity
import ai.timefold.solver.core.api.domain.variable.PlanningVariable
import java.math.BigDecimal

@PlanningEntity
data class NetAmountWithTax @JvmOverloads constructor(val netAmount: NetAmount = NetAmount(BigDecimal.ZERO)) {

  @PlanningVariable
  var taxRate: TaxRate? = null

  val taxAmount: TaxAmount get() = netAmount.taxAt(taxRate ?: TaxRate.PCT_00)
  val grossAmount: GrossAmount get() = netAmount.grossAt(taxRate ?: TaxRate.PCT_00)
  override fun toString() = "NetAmountWithTax(taxRate=$taxRate, netAmount=$netAmount)"

}
