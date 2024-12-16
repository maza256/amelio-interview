package tech.amelio.interview.stocks.controller

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import tech.amelio.interview.stocks.models.StockModel
import tech.amelio.interview.stocks.service.StockConsumer


@ExtendWith(MockKExtension::class)
class StockControllerTest {

    private val stockConsumer: StockConsumer = mockk()
    private val popularStockController = PopularStocksController(stockConsumer)
    private val stocksController = StocksController(stockConsumer)
    private val sumStocksController = SumStocksController(stockConsumer)

    @Test
    fun test_getStock() {
        val stockName = "stock-1"
        val stockModel = StockModel(name = stockName, price = 150, queryCount = 0)
        every { stockConsumer.getStock(stockName) } returns stockModel

        val price = stocksController.getStockValue(stockName)

        assertEquals(150, price, "The stock price returned should match.")
    }

    @Test
    fun test_getStock_invalid_stock() {
        val stockName = "INVALID_STOCK"
        every { stockConsumer.getStock(stockName) } returns null
        val exception = assertThrows<IllegalArgumentException> {
            stocksController.getStockValue(stockName)
        }
        assertEquals("Stock not found", exception.message, "Exception message should indicate stock not found.")
    }

    @Test
    fun test_getPopularStocks() {
        val popularStocks = listOf("stock-1", "stock-2", "stock-3")
        every { stockConsumer.getPopularStocks() } returns popularStocks

        val result = popularStockController.getPopularStocks()
        assertEquals(popularStocks, result, "The popular stocks returned did not match the expected value.")
    }

    @Test
    fun test_getSumOfStocks() {
        every { stockConsumer.getSumOfStocks() } returns 5000

        val result = sumStocksController.getSum()
        assertEquals(5000, result, "The sum of stock prices did not match the expected value.")
    }
}