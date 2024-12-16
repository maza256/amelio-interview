package tech.amelio.interview.stocks.service


import jakarta.annotation.PostConstruct
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.scheduling.annotation.Scheduled
import tech.amelio.interview.stocks.models.StockMessage
import tech.amelio.interview.stocks.models.StockModel
import java.time.ZoneOffset
import java.util.concurrent.ConcurrentLinkedQueue

@Service
@Async
class StockConsumer(
    private val queue: ConcurrentLinkedQueue<StockMessage>,
    private val stockNameList: List<String>
) : Runnable {

    private var stocks = mutableMapOf<String, StockModel>().apply {
        stockNameList.forEach { name ->
            this[name] = StockModel(name = name, price = 1000, queryCount = 0)
        }
    }

    private val messageCountExpected = stockNameList.size
    private val lock = Any()
    private var sumOfStocks = 0L

    private val groupedStockData = HashMap<Int, HashMap<String, StockModel>>()


    private fun setSumOfStocks(newSum: Long) {
        sumOfStocks = newSum
    }

    override fun run() {
        while (true) {
            try {
                var currentSumStocks = 0L

                val stocksTempData = stocks.mapValues { it.value.copy() }.toMutableMap()

                while (true) {
                    val message = queue.poll()
                    if (message != null) {
                        val seconds: Int = message.timestamp.atZone(ZoneOffset.UTC).second

                        var stockModel = StockModel(name = message.stockName, price = message.price, queryCount = 0)

                        groupedStockData.getOrPut(seconds, { HashMap() })
                        groupedStockData[seconds]?.set(message.stockName, stockModel)


                        if (groupedStockData[seconds]?.size == messageCountExpected) {
                            currentSumStocks = groupedStockData[seconds]?.values?.map { it.price }?.sum() ?: 0L

                            synchronized(lock) {
                                setSumOfStocks(currentSumStocks)
                                stocks = mergeMapData(groupedStockData[seconds]!!)
                            }
                            groupedStockData.remove(seconds)
                        }
                    } else {
                        Thread.sleep(100)
                    }
                }
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                break
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
            return stocks[name]?.apply { queryCount++ }
        }
    }

    fun getPopularStocks(): List<String> {
        synchronized(lock) {
            return stocks.values.sortedByDescending { it.queryCount }.take(3).map { it.name }
        }
    }

    fun getSumOfStocks(): Long {
        synchronized(lock) {
            return sumOfStocks
        }
    }
}
