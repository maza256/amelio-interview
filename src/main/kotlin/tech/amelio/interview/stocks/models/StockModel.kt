package tech.amelio.interview.stocks.models

data class StockModel(
    val name: String,
    var price: Long,
    var queryCount: Int = 0
)