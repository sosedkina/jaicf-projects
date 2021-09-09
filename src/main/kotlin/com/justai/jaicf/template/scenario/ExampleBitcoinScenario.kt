package com.justai.jaicf.template.scenario

import com.justai.jaicf.builder.Scenario
import com.justai.jaicf.template.http.httpClient
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking

data class BitcoinToUSD(val USD: Record) {
    data class Record(val buy: Double, val sell: Double)
}

val ExampleBitcoinScenario = Scenario {
    state("getBitcoinPrice") {
        action {
            val data = runBlocking {
                httpClient.get<BitcoinToUSD>("https://blockchain.info/ticker")
            }
            reactions.say("You can buy BitCoin for ${data.USD.buy}$")
        }
    }
}

