import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tech.amelio.interview.stocks.models.StockMessage
import tech.amelio.interview.stocks.service.StockConsumer
import java.time.Instant
import java.util.concurrent.ConcurrentLinkedQueue

class StockConsumerTest {

    private val queue = ConcurrentLinkedQueue<StockMessage>()
    private val stockNameList = listOf("stock-1", "stock-2", "stock-3", "stock-4")
    private val stockConsumer = StockConsumer(queue, stockNameList)

    @Test
    fun test_getStock() {
        val stockName = "stock-1"

        var stock1 = stockConsumer.getStock(stockName)
        assertEquals(stock1?.queryCount, 1)

        stock1 = stockConsumer.getStock(stockName)
        assertEquals(stock1?.queryCount, 2)
    }

    @Test
    fun test_checkPopularStocks() {
        repeat(10) { stockConsumer.getStock("stock-4") } // Increment stock-1 queries
        repeat(7) { stockConsumer.getStock("stock-3") } // Increment stock-2 queries
        repeat(5) { stockConsumer.getStock("stock-2") } // Incremen

        val popularStocks = stockConsumer.getPopularStocks()
        assertEquals(listOf("stock-4", "stock-3", "stock-2"), popularStocks)
    }

    @Test
    fun test_checkSumOfStocks() {
        val messages = listOf(
            StockMessage(Instant.now(), 1200,"stock-1"),
            StockMessage(Instant.now(), 1500,"stock-2"),
            StockMessage(Instant.now(), 900,"stock-3"),
            StockMessage(Instant.now(), 200,"stock-4"),

            )
        messages.forEach { queue.add(it) }

        val thread = Thread(stockConsumer)

        thread.start()
        Thread.sleep(500) // Let the stock service process messages
        thread.interrupt()
        thread.join()

        val sum = stockConsumer.getSumOfStocks()
        assertEquals(3800, sum, "The sum of stocks did not match expected. Should be 3800, received $sum.")
    }
}