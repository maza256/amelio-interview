package tech.amelio.interview.stocks.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.amelio.interview.stocks.service.StockConsumer

@RestController
@RequestMapping("/popular-stocks")
class PopularStocksController(private val stockConsumer: StockConsumer) {
    @GetMapping
    fun getPopularStocks(): List<String> {
        return stockConsumer.getPopularStocks()
    }
}