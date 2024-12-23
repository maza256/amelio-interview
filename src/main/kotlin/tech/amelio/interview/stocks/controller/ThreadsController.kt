package tech.amelio.interview.stocks.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/thread")
class ThreadsController {
    @GetMapping("/name")
    fun getThreadName(): String {
        return Thread.currentThread().toString()
    }
}