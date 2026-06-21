package com.kronos

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KronosApplication

fun main(args: Array<String>) {
    runApplication<KronosApplication>(*args)
}
