package tech.amelio.interview.stocks.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.amelio.interview.stocks.service.StockConsumer

@RestController
@RequestMapping("/stocks")
class StocksController(private val stockConsumer: StockConsumer) {
    @GetMapping("{stockName}")
    fun getStockValue(@PathVariable stockName: String): Long {
        val stock = stockConsumer.getStock(stockName)
            ?: throw IllegalArgumentException("Stock not found")
        return stock.price
    }
}