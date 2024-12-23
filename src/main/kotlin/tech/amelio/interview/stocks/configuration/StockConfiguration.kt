package tech.amelio.interview.stocks.configuration

import kotlinx.coroutines.channels.Channel
import org.apache.coyote.ProtocolHandler
import org.apache.coyote.http11.Http11NioProtocol
import org.apache.tomcat.util.net.AbstractEndpoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer
import org.springframework.scheduling.annotation.EnableAsync
import tech.amelio.interview.stocks.models.StockMessage
import tech.amelio.interview.stocks.service.StockConsumer
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors

@Configuration
@EnableAsync
class StockConfiguration {

    @Value("\${stock.service.count:10}")
    private val numServices: Int = 0

//    @Bean
//    fun stockMessageQueue(): ConcurrentLinkedQueue<StockMessage> {
//        return ConcurrentLinkedQueue<StockMessage>()
//    }

    @Bean
    fun stockMessageQueue(): Channel<StockMessage> {
        return Channel<StockMessage>()
    }

    @Bean
    fun stockNameList(): List<String> {
        return List(numServices) { i -> "stock-${i+1}"}
    }

    @Bean
    fun runner(
        stockConsumer: StockConsumer
    ): CommandLineRunner {
        return CommandLineRunner {
            print("Starting $numServices instances of the stock service...")
        }
    }

    @Bean
    fun virtualThreadCustomizer(): TomcatProtocolHandlerCustomizer<ProtocolHandler> {
        return TomcatProtocolHandlerCustomizer { protocolHandler ->
            protocolHandler.executor = Executors.newVirtualThreadPerTaskExecutor()
        }
    }
}