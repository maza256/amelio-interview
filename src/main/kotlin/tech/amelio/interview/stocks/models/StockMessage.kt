package tech.amelio.interview.stocks.models

import java.time.Instant

data class StockMessage(
    val timestamp: Instant,
    val price: Long,
    var stockName: String
)