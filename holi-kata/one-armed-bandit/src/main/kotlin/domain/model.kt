package io.github.jangalinski.kata.onearmedbandit.domain

import java.util.Random

/**
 * Represents a single slot wheel symbol with its payout value.
 * Possible values are APPLE, BANANA, and CLEMENTINE.
 * Use [random] to get a random symbol.
 */
enum class SlotWheel(val payout: Int) {
    APPLE(10),
    BANANA(15),
    CLEMENTINE(20),
    ;

    companion object {
        /**
         * Returns a random [SlotWheel] symbol using the provided [Random] instance.
         */
        fun random(random: Random = Random()): SlotWheel =
            entries[random.nextInt(entries.size)]
    }
}

/**
 * Represents the result of a slot machine spin as a triple of [SlotWheel] symbols.
 * Provides a [payout] function to calculate the win based on the result.
 *
 * @property first the symbol of the first wheel
 * @property second the symbol of the second wheel
 * @property third the symbol of the third wheel
 */
data class SlotResult(val first: SlotWheel, val second: SlotWheel, val third: SlotWheel) {
    /**
     * Creates a [SlotResult] with three random [SlotWheel] symbols.
     */
    constructor(random: Random = Random()) : this(
        first = SlotWheel.random(random),
        second = SlotWheel.random(random),
        third = SlotWheel.random(random)
    )

    /**
     * Returns the payout for this result. If all three symbols are equal, returns the payout value of the symbol, otherwise 0.
     */
    fun payout(): Int = if (first == second && second == third) first.payout else 0
}
