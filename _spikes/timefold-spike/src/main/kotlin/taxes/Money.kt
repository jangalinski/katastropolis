package io.github.jangalinski.spike.timefold.taxes

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Policy for monetary rounding/scale.
 * - Monetary values print with scale 2 and HALF_UP.
 */
internal object MoneyMath {
  val SCALE_DISPLAY = 2
  val ROUNDING_DISPLAY: RoundingMode = RoundingMode.HALF_UP
}

/** A tax rate expressed as a fraction (e.g. 0.19 for 19%). */
enum class TaxRate(val fraction: BigDecimal) {
  PCT_00(BigDecimal.ZERO),
  PCT_07(0.07.toBigDecimal()),
  PCT_19(0.19.toBigDecimal()),
  ;

  init {
    require(fraction >= BigDecimal.ZERO) { "TaxRate must be >= 0" }
  }

  override fun toString(): String = "TaxRate(${fraction.movePointRight(2).setScale(2, MoneyMath.ROUNDING_DISPLAY)} %)"
}

@JvmInline
value class NetAmount(val amount: BigDecimal) {
  fun rounded(): BigDecimal = amount.setScale(MoneyMath.SCALE_DISPLAY, MoneyMath.ROUNDING_DISPLAY)
  override fun toString(): String = "NetAmount(${rounded()})"

  operator fun plus(other: NetAmount): NetAmount = NetAmount(this.amount + other.amount)
  operator fun minus(other: NetAmount): NetAmount = NetAmount(this.amount - other.amount)

  fun taxAt(rate: TaxRate): TaxAmount = TaxAmount(amount.multiply(rate.fraction))
  fun grossAt(rate: TaxRate): GrossAmount = GrossAmount(amount.multiply(1.toBigDecimal() + rate.fraction))

}

@JvmInline
value class GrossAmount(val amount: BigDecimal) {
  fun rounded(): BigDecimal = amount.setScale(MoneyMath.SCALE_DISPLAY, MoneyMath.ROUNDING_DISPLAY)
  override fun toString(): String = "GrossAmount(${rounded()})"

  operator fun plus(other: GrossAmount): GrossAmount = GrossAmount(this.amount + other.amount)
  operator fun minus(other: GrossAmount): GrossAmount = GrossAmount(this.amount - other.amount)
  operator fun minus(taxAmount: TaxAmount): NetAmount = NetAmount(this.amount - taxAmount.amount)

  fun netAt(rate: TaxRate): NetAmount = NetAmount(amount.divide(1.toBigDecimal() + rate.fraction, 10, RoundingMode.HALF_UP))
  fun taxAt(rate: TaxRate): TaxAmount = TaxAmount(this.amount - this.netAt(rate).amount)
}

@JvmInline
value class TaxAmount(val amount: BigDecimal) {
  fun rounded(): BigDecimal = amount.setScale(MoneyMath.SCALE_DISPLAY, MoneyMath.ROUNDING_DISPLAY)
  override fun toString(): String = "TaxAmount(${rounded()})"

  operator fun plus(other: TaxAmount): TaxAmount = TaxAmount(this.amount + other.amount)
  operator fun minus(other: TaxAmount): TaxAmount = TaxAmount(this.amount - other.amount)

  fun netAt(rate: TaxRate): NetAmount = NetAmount(amount.divide(rate.fraction, 10, RoundingMode.HALF_UP))
  fun grossAt(rate: TaxRate): GrossAmount = GrossAmount(netAt(rate).amount + amount)
}
