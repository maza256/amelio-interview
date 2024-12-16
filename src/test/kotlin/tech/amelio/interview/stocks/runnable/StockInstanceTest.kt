import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.amelio.interview.stocks.models.StockMessage
import tech.amelio.interview.stocks.runnable.StockInstance

import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.test.assertEquals

class StockInstanceTest {

    private val queue: ConcurrentLinkedQueue<StockMessage> = mockk(relaxed = true)

    @Test
    fun shouldInitializeWithInitialPrice() {
        val instanceName = "stock-1"
        val initialPrice = 1000L
        val sleepTime = 100L
        val stockRunnerService = StockInstance(instanceName, initialPrice, queue)
        val thread = Thread(stockRunnerService)

        thread.start()
        Thread.sleep(sleepTime)
        stockRunnerService.stop()
        thread.join()
        verify {
            queue.add(match { stockMessage ->
                stockMessage.stockName == "stock-1"
            }
            )
        }
    }
}