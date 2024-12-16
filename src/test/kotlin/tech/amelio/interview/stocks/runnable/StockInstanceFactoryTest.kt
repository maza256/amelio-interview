import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import tech.amelio.interview.stocks.models.StockMessage
import tech.amelio.interview.stocks.runnable.StockInstanceFactory
import java.util.concurrent.ConcurrentLinkedQueue

class StockInstanceFactoryTest {

    private lateinit var stockInstanceFactory: StockInstanceFactory
    private lateinit var stockMessageQueue: ConcurrentLinkedQueue<StockMessage>

    @BeforeEach
    fun setUp() {
        stockInstanceFactory = StockInstanceFactory()
        stockMessageQueue = ConcurrentLinkedQueue()
    }

    @Test
    fun test_createStockInstances() {
        val threads = stockInstanceFactory.createStockInstances(
                    numServices = 5, 
                    initialPrice = 1000L, 
                    stockMessageQueue = stockMessageQueue)
        
        assertEquals(5, threads.size)
    }
}