package io.github.jangalinski.kata.onearmedbandit.domain

import java.util.*

sealed interface Game {
    val id: UUID
    val history: List<SlotResult>
}

data class ActiveGame(
    override val id: UUID = UUID.randomUUID(),
    val coins: Coin,
    override val history: List<SlotResult> = emptyList(),
    private val random: Random = Random(),
    val bet: Coin = Coin(3)
) : Game {
    fun changeBet(newBet: Coin): ActiveGame {
        require(newBet.value >= 3) { "Bet must be at least 3" }
        require(newBet.value <= coins.value) { "Bet cannot be greater than available coins" }
        return copy(bet = newBet)
    }

    fun pullLever(): ActiveGame {
        require(coins.value >= bet.value) { "Not enough coins to play" }
        val newCoins = coins - bet
        val result = SlotResult(random)
        val updatedCoins = newCoins + Coin(result.payout * (bet.value / 3))
        return copy(
            coins = updatedCoins,
            history = history + result
        )
    }

    fun increaseCoins(amount: Int): ActiveGame {
        require(amount > 0) { "Amount must be positive" }
        return copy(coins = coins + Coin(amount))
    }

    fun decreaseCoins(amount: Int): Pair<ActiveGame, Coin> {
        require(amount > 0) { "Amount must be positive" }
        val payout = Coin(amount.coerceAtMost(coins.value))
        return copy(coins = coins - payout) to payout
    }

    fun endGame(): EndedGame = EndedGame(id = id, history = history)
}

data class EndedGame(
    override val id: UUID,
    override val history: List<SlotResult>
) : Game
