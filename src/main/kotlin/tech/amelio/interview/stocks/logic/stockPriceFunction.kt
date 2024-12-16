package tech.amelio.interview.stocks.logic

import kotlin.math.max
import kotlin.random.Random.Default.nextInt

/**
 * Given the value of a stock at any given second, returns the price of the given stock for the next second
 * The new value is always a positive integer
 * Simulates a very expensive computational process by blocking the thread during 0.8 seconds
 */
fun getNextStockPrice(name: String, value: Long): Long {
    Thread.sleep(800)
    return max(1, value + nextInt(-10, 11))
}