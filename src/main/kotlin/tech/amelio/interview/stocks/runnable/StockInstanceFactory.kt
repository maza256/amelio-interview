package tech.amelio.interview.stocks.runnable

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import tech.amelio.interview.stocks.models.StockMessage
import java.util.concurrent.ConcurrentLinkedQueue

@Service
class StockInstanceFactory {

    @Bean
    fun startStockInstances(
        @Value("\${stock.service.count:10}") numServices: Int,
        @Value("\${stock.runner.initialprice:1000}") initialPrice: Long,
        stockInstanceFactory: StockInstanceFactory,
        stockMessageQueue: ConcurrentLinkedQueue<StockMessage>,
    ): List<Thread> {
        val stockThreads = stockInstanceFactory.createStockInstances(numServices, initialPrice, stockMessageQueue)
        stockThreads.forEach { it.start() }
        return stockThreads
    }

    fun createStockInstances(
        numServices: Int,
        initialPrice: Long,
        stockMessageQueue: ConcurrentLinkedQueue<StockMessage>
    ): List<Thread> {

        val stockServicesAndThreads = mutableListOf<Thread>()

        for (i in 1..numServices) {
            val service = StockInstance("stock-$i", initialPrice, stockMessageQueue)
            val thread = Thread(service)
            stockServicesAndThreads.add(thread)
        }

        return stockServicesAndThreads
    }

}