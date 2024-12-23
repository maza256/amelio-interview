package tech.amelio.interview.stocks

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.SpringApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import tech.amelio.interview.stocks.models.StockMessage
import tech.amelio.interview.stocks.runnable.StockInstance
import tech.amelio.interview.stocks.runnable.StockInstanceFactory
import tech.amelio.interview.stocks.service.StockConsumer
import java.util.concurrent.ConcurrentLinkedQueue

@SpringBootApplication
class AmelioStockApplication

fun main(args: Array<String>) {
    SpringApplication.run(AmelioStockApplication::class.java, *args)
}
