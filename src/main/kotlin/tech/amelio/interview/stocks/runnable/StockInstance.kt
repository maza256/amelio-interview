package tech.amelio.interview.stocks.runnable

import tech.amelio.interview.stocks.logic.getNextStockPrice
import tech.amelio.interview.stocks.models.StockMessage
import java.time.Instant
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.math.max
import kotlin.random.Random.Default.nextInt
import kotlin.random.Random.Default.nextLong

class StockInstance(private val instanceName: String,
                         private val initialPrice: Long,
                         private val queue: ConcurrentLinkedQueue<StockMessage>,
) : Runnable {
    private var running = true
    private var currentPrice: Long = initialPrice // Initial price

    override fun run() {
        println("Starting StockRunnerService: $instanceName with initial price $currentPrice")

        while (running) {
            currentPrice = getNextStockPrice(instanceName, currentPrice)
            try {
                val stockMessage = StockMessage(Instant.now(), currentPrice, instanceName)
                queue.add(stockMessage)
            } catch(e: InterruptedException) {
                Thread.currentThread().interrupt()
                break
            }
        }

        println("StockRunnerService: $instanceName stopped.")
    }

    fun stop() {
        running = false
    }
}

