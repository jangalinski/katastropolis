package io.github.jangalinski.kata.onearmedbandit.domain

import java.util.UUID

sealed interface HistoryEvent {
    val gameId: UUID
}

data class GameStarted(
    override val gameId: UUID,
    val initialCoins: Coin
) : HistoryEvent

data class LeverPulled(
    override val gameId: UUID,
    val result: SlotResult,
    val bet: Int
) : HistoryEvent

data class BetChanged(
    override val gameId: UUID,
    val oldBet: Int,
    val newBet: Int
) : HistoryEvent

data class CoinsIncreased(
    override val gameId: UUID,
    val amount: Coin
) : HistoryEvent

data class CoinsDecreased(
    override val gameId: UUID,
    val amount: Coin
) : HistoryEvent

data class GameEnded(
    override val gameId: UUID,
    val payout: Coin
) : HistoryEvent

