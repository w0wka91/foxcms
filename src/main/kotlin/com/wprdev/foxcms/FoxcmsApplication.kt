package com.wprdev.foxcms

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FoxcmsApplication

fun main(args: Array<String>) {
    runApplication<FoxcmsApplication>(*args)
}

