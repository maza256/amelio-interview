package tech.amelio.interview.stocks.runnable

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import tech.amelio.interview.stocks.logic.getNextStockPrice
import tech.amelio.interview.stocks.models.StockMessage
import java.time.Instant
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.math.max
import kotlin.random.Random.Default.nextInt
import kotlin.random.Random.Default.nextLong
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

class StockInstance(private val instanceName: String,
                    private val initialPrice: Long,
                    private val queue: Channel<StockMessage>,
) {
    private var running = true
    private var currentPrice: Long = initialPrice // Initial price

    suspend fun run() {
        println("Starting StockRunnerService: $instanceName with initial price $currentPrice")

        while (running) {
            currentPrice = getNextStockPrice(instanceName, currentPrice)
            println("Next price of $instanceName is $currentPrice")
            try {
                val stockMessage = StockMessage(Instant.now(), currentPrice, instanceName)
                queue.trySend(stockMessage)
            } catch(e: InterruptedException) {
                print("I've been interrupted")
                Thread.currentThread().interrupt()
                break
            }
        }

        println("StockRunnerService: $instanceName stopped.")
    }

    fun stop() {
        println("Time to stop $instanceName")
        running = false
    }
}

