package tech.amelio.interview.stocks.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.amelio.interview.stocks.service.StockConsumer


@RestController
@RequestMapping("/popular-stocks")
class PopularStocksController(private val stockConsumer: StockConsumer) {
    @GetMapping
    fun getPopularStocks(): String {

        // Convert to JSON
        val objectMapper = ObjectMapper()
        return objectMapper.writeValueAsString(stockConsumer.getPopularStocks())
    }
}