package tech.amelio.interview.stocks.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.amelio.interview.stocks.service.StockConsumer

@RestController
@RequestMapping("/sum-stocks")
class SumStocksController(private val stockConsumer: StockConsumer) {
    @GetMapping
    fun getSum(): Long {
        return stockConsumer.getSumOfStocks()
    }
}