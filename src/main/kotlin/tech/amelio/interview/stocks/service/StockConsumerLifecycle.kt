package tech.amelio.interview.stocks.service

import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
class StockConsumerLifecycle(
   private val stockConsumer: StockConsumer
) : InitializingBean {
    override fun afterPropertiesSet() {
        
    }
//   private val thread = stockConsumer.startConsuming()

//    override fun afterPropertiesSet() {
//        thread.start()
//    }
}