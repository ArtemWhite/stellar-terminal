package com.artemwhite.satellite

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SatelliteServiceApplication

fun main(args: Array<String>) {
    runApplication<SatelliteServiceApplication>(*args)
}
