import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import tech.amelio.interview.stocks.service.StockConsumer
import tech.amelio.interview.stocks.service.StockConsumerLifecycle

class StockConsumerLifecycleTest {
    private lateinit var stockConsumer: StockConsumer
    private lateinit var stockConsumerLifecycle: StockConsumerLifecycle

    @BeforeEach
    fun setUp() {
        // Mock your StockConsumer
        stockConsumer = mock(StockConsumer::class.java)

        // Initialize your StockConsumerLifecycle with mocked StockConsumer
        stockConsumerLifecycle = StockConsumerLifecycle(stockConsumer)
    }

    @Test
    fun test_afterPropertiesSet() {
        doNothing().`when`(stockConsumer).run()

        stockConsumerLifecycle.afterPropertiesSet()

        verify(stockConsumer).run()
    }
}