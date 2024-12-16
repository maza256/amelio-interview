package tech.amelio.interview.stocks.logic

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class StockPriceFunctionTest {

    @Test
    fun test_stockPriceStaysPositive() {
        val name = "stock-1"
        val value = 5L

        repeat(5) {
            print("here")
            val newValue = getNextStockPrice(name, value)
            assertTrue(newValue >= 1, "Stock price should have remained positive")
        }
    }

    @Test
    fun test_stockPriceChanges() {
        val name = "stock-1"
        val value = 50L

        repeat(5) {
            val newValue = getNextStockPrice(name, value)
            assertTrue(newValue in (value - 10)..(value + 10),
                "Stock price should stay within the random range of value ±10"
            )
        }
    }
}