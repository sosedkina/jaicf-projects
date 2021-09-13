package com.justai.jaicf.template.http

import com.justai.jaicf.template.serializers.Jackson
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*

val httpClient = HttpClient(CIO) {
    expectSuccess = true
    engine {
        endpoint {
            connectTimeout = 3000
            requestTimeout = 30000
            keepAliveTime = 3000
            connectAttempts = 3
        }
    }
    install(Logging) {
        this.logger = Logger.DEFAULT
        this.level = LogLevel.INFO
    }
    install(JsonFeature) {
        serializer = JacksonSerializer(Jackson.mapper)
    }
}

