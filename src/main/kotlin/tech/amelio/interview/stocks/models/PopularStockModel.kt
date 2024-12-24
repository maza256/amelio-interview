package tech.amelio.interview.stocks.models

class PopularStockModel(private val popularStocks: List<String>, private val timestamp: String) {

    fun getTimestamp(): String {
        return timestamp
    }
    fun getPopularStocks(): List<String> {
        return popularStocks
    }


}