package tech.amelio.interview.stocks.service

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.springframework.stereotype.Service
import tech.amelio.interview.stocks.models.PopularStockModel
import tech.amelio.interview.stocks.models.StockMessage
import tech.amelio.interview.stocks.models.StockModel
import java.time.ZoneOffset
import java.util.concurrent.atomic.AtomicLong

@Service
class StockConsumer(
    private val channel: Channel<StockMessage>,  // Using channel instead of ConcurrentLinkedQueue
    private val stockNameList: List<String>,
    private val coroutineScope: CoroutineScope
) {

    @PostConstruct
    fun init() {
        // Start your coroutine task when the application initializes
        coroutineScope.launch {
            startConsuming()
        }
    }


    private var stocks = mutableMapOf<String, StockModel>().apply {
        stockNameList.forEach { name ->
            this[name] = StockModel(name = name, price = 1000, queryCount = 0)
        }
    }

    private var stocksQueries = mutableMapOf<String, StockModel>().apply {
        stockNameList.forEach { name ->
            this[name] = StockModel(name = name, price = 1000, queryCount = 0)
        }
    }

    private val messageCountExpected = stockNameList.size
    private val lock = Any()
    private var sumOfStocks = AtomicLong(0L)
    private var publishedSecond = AtomicLong(0L)

    private val groupedStockData = HashMap<Int, HashMap<String, StockModel>>()

    private fun setSumOfStocks(newSum: Long) {
        sumOfStocks.set(newSum)
    }

    private fun processMessages(message: StockMessage) {
        val seconds: Int = message.timestamp.atZone(ZoneOffset.UTC).second

        var stockModel = StockModel(name = message.stockName, price = message.price, queryCount = 0)

        groupedStockData.getOrPut(seconds) { HashMap() }
        groupedStockData[seconds]?.set(message.stockName, stockModel)

        if (groupedStockData[seconds]?.size == messageCountExpected &&
            seconds.toLong() != publishedSecond.toLong()
        ) {
            val newSumOfStocks = groupedStockData[seconds]?.values?.map { it.price }?.sum() ?: 0L
            setSumOfStocks(newSumOfStocks)
            synchronized(lock) {
                stocks = groupedStockData[seconds]!!
            }
            publishedSecond.set(seconds.toLong())
            groupedStockData.remove(seconds)
        }
    }

    suspend fun startConsuming() {
        coroutineScope {
            launch(Dispatchers.Default) {
                while (true) {
                    try {
                        val message = channel.tryReceive().getOrNull()
                        if(message != null) {
                            processMessages(message)
                        }
                    } catch (e: Exception) {
                        println("Error consuming message: ${e.message}")
                    }
                }
            }
        }
    }

    private fun mergeMapData(newMap: MutableMap<String, StockModel>): MutableMap<String, StockModel> {
        val mergedMap = hashMapOf<String, StockModel>()
        for ((key, tempStock) in newMap) {
            val queryCount = stocks[key]?.queryCount ?: 0
            mergedMap[key] = StockModel(
                name = tempStock.name,
                price = tempStock.price,
                queryCount = queryCount
            )
        }
        return mergedMap
    }

    fun getStock(name: String): StockModel? {
        synchronized(lock) {
            stocksQueries[name]?.apply { queryCount++ }
            return stocks[name]
        }
    }

    fun getPopularStocks(): PopularStockModel {
        synchronized(lock) {
            var popularStocks = stocksQueries.values.sortedByDescending { it.queryCount }.take(3).map { it.name }
            var stockModel =  PopularStockModel(popularStocks, publishedSecond.toString())
            return stockModel
        }
    }

    fun getSumOfStocks(): Long {
        synchronized(lock) {
            return sumOfStocks.toLong()
        }
    }
}


// package tech.amelio.interview.stocks.service


// import jakarta.annotation.PostConstruct
// import org.springframework.scheduling.annotation.Async
// import org.springframework.stereotype.Service
// import org.springframework.scheduling.annotation.Scheduled
// import tech.amelio.interview.stocks.models.StockMessage
// import tech.amelio.interview.stocks.models.StockModel
// import java.time.ZoneOffset
// import java.util.concurrent.ConcurrentLinkedQueue
// import java.util.concurrent.atomic.AtomicLong

// @Service
// @Async
// class StockConsumer(
//     private val queue: ConcurrentLinkedQueue<StockMessage>,
//     private val stockNameList: List<String>
// ) : Runnable {

//     private var stocks = mutableMapOf<String, StockModel>().apply {
//         stockNameList.forEach { name ->
//             this[name] = StockModel(name = name, price = 1000, queryCount = 0)
//         }
//     }

//     private var stocksQueries = mutableMapOf<String, StockModel>().apply {
//         stockNameList.forEach { name ->
//             this[name] = StockModel(name = name, price = 1000, queryCount = 0)
//         }
//     }

//     private val messageCountExpected = stockNameList.size
//     private val lock = Any()
//     private var sumOfStocks = AtomicLong(0L)

//     private val groupedStockData = HashMap<Int, HashMap<String, StockModel>>()


//     private fun setSumOfStocks(newSum: Long) {
//         sumOfStocks.set(newSum)
//     }

//     override fun run() {
//         while (true) {
//             try {
// //                val stocksTempData = stocks.mapValues { it.value.copy() }.toMutableMap()

//                 while (true) {
//                     val message = queue.poll()
//                     if (message != null) {
//                         val seconds: Int = message.timestamp.atZone(ZoneOffset.UTC).second

//                         var stockModel = StockModel(name = message.stockName, price = message.price, queryCount = 0)

//                         groupedStockData.getOrPut(seconds, { HashMap() })
//                         groupedStockData[seconds]?.set(message.stockName, stockModel)


//                         if (groupedStockData[seconds]?.size == messageCountExpected) {
//                             val newSumOfStocks = groupedStockData[seconds]?.values?.map { it.price }?.sum() ?: 0L
//                             setSumOfStocks(newSumOfStocks)
//                             synchronized(lock) {    
//                                 stocks = groupedStockData[seconds]!!
// //                                stocks = mergeMapData(groupedStockData[seconds]!!)
//                             }
//                             groupedStockData.remove(seconds)
//                         }
//                     } else {
//                         Thread.sleep(100)
//                     }
//                 }
//             } catch (e: InterruptedException) {
//                 Thread.currentThread().interrupt()
//                 break
//             }
//         }
//     }

//     private fun mergeMapData(newMap: MutableMap<String, StockModel>): MutableMap<String, StockModel> {
//         val mergedMap = hashMapOf<String, StockModel>()
//         for ((key, tempStock) in newMap) {
//             val queryCount = stocks[key]?.queryCount ?: 0
//             mergedMap[key] = StockModel(
//                 name = tempStock.name,
//                 price = tempStock.price,
//                 queryCount = queryCount
//             )
//         }
//         return mergedMap
//     }

//     fun getStock(name: String): StockModel? {
//         synchronized(lock) {
//             return stocksQueries[name]?.apply { queryCount++ }
//         }
//     }

//     fun getPopularStocks(): List<String> {
//         synchronized(lock) {
//             return stocksQueries.values.sortedByDescending { it.queryCount }.take(3).map { it.name }
//         }
//     }

//     fun getSumOfStocks(): Long {
//         synchronized(lock) {
//             return sumOfStocks.toLong()
//         }
//     }
// }
