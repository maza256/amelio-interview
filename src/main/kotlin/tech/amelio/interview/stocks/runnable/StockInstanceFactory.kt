package tech.amelio.interview.stocks.runnable

import kotlinx.coroutines.channels.Channel
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import tech.amelio.interview.stocks.models.StockMessage
import java.util.concurrent.ConcurrentLinkedQueue
import kotlinx.coroutines.*

@Service
class StockInstanceFactory {

    @Bean
    fun startStockInstances(
        @Value("\${stock.service.count:10}") numServices: Int,
        @Value("\${stock.runner.initialprice:1000}") initialPrice: Long,
        stockInstanceFactory: StockInstanceFactory,
        stockMessageQueue: Channel<StockMessage>,
    ) : Int {
        val stockJobs = stockInstanceFactory.createStockInstances(numServices, initialPrice, stockMessageQueue)
        stockJobs.forEach({ it.start() })
            //        stockThreads.forEach { it.start() }
        return stockJobs.size
    }

    fun createStockInstances(
        numServices: Int,
        initialPrice: Long,
        stockMessageQueue: Channel<StockMessage>
    ): List<Job> {

        val stockJobs = mutableListOf<Job>()

        for (i in 1..numServices) {
            val service = StockInstance("stock-$i", initialPrice, stockMessageQueue)
            val job = CoroutineScope(Dispatchers.Default).launch {
                service.run()
            }
            stockJobs.add(job)
        }

        return stockJobs
    }

}