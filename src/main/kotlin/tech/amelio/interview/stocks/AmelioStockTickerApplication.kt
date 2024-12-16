package tech.amelio.interview.stocks

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import tech.amelio.interview.stocks.models.StockMessage
import tech.amelio.interview.stocks.runnable.StockInstance
import tech.amelio.interview.stocks.runnable.StockInstanceFactory
import tech.amelio.interview.stocks.service.StockConsumer
import java.util.concurrent.ConcurrentLinkedQueue

@SpringBootApplication
class AmelioStockApplication(
    @Value("\${stock.service.count:10}") private val numServices: Int,
    @Value("\${stock.runner.initialprice:1000}") private val initialPrice: Long
) {
    private val stockServices = mutableListOf<StockInstance>()
    private val serviceThreads = mutableListOf<Thread>()

    @Bean
    fun stockMessageQueue(): ConcurrentLinkedQueue<StockMessage> {
        return ConcurrentLinkedQueue<StockMessage>()
    }

    @Bean
    fun stockNameList(): List<String> {
        return List(numServices) { i -> "stock-${i+1}"}
    }



    @Bean
    fun runner(
        stockConsumer: StockConsumer
    ): CommandLineRunner {
        return CommandLineRunner {
            print("Starting $numServices instances of the stock service...")
        }
    }
}

fun main(args: Array<String>) {
    runApplication<AmelioStockApplication>(*args)
}
