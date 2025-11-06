package io.github.jangalinski.spike.timefold.taxes

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore
import ai.timefold.solver.core.api.score.stream.Constraint
import ai.timefold.solver.core.api.score.stream.ConstraintCollectors.sumBigDecimal
import ai.timefold.solver.core.api.score.stream.ConstraintFactory
import ai.timefold.solver.core.api.score.stream.ConstraintProvider

class TaxRateConstraintProvider() : ConstraintProvider {

  override fun defineConstraints(constraintFactory: ConstraintFactory): Array<Constraint> = arrayOf(
    matchTargetTotalGross(constraintFactory),
    minimizeTotalGross(constraintFactory)
  )

  private fun matchTargetTotalGross(cf: ConstraintFactory): Constraint =
    cf.forEach(NetAmountWithTax::class.java)
      .groupBy(sumBigDecimal { it.grossAmount.amount })
      .join(cf.forEach(NetAmountWithTaxList.Total::class.java))
      .filter { totalGross, target -> totalGross.compareTo(target.totalGross.amount) != 0 }
      .penalize(HardSoftScore.ONE_HARD)
      .asConstraint("Match target total gross")

  private fun minimizeTotalGross(cf: ConstraintFactory): Constraint =
    cf.forEach(NetAmountWithTax::class.java)
      .groupBy(sumBigDecimal { it.grossAmount.amount })
      .penalize(HardSoftScore.ONE_SOFT) { totalGross -> totalGross.intValueExact() }
      .asConstraint("Minimize total gross")

}
