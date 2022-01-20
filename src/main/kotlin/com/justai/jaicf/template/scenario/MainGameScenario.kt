package com.justai.jaicf.template.scenario

import com.justai.jaicf.builder.Scenario
import com.justai.jaicf.template.http.httpClient
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking

data class BitcoinToUSD(val USD: Record) {
    data class Record(val buy: Double, val sell: Double)
}

val MainGameScenario = Scenario {
    state("game") {
        action {
            reactions.say("Поехали!")
            reactions.go("/giveCountry")
        }
    }

    state("giveCountry") {
        action {
            var countryGiven = "Великобритания"
            reactions.say("Какой город является столицей государства ${countryGiven}?")
        }


        state("cityGiven") {
            activators {
                intent("City")
            }

            action {
                var cityGiven = request.input.capitalize()
                reactions.say("Если честно, я и сам не знаю, является ли $cityGiven столицей.")
                reactions.go("/giveCountry")
            }
        }

        fallback {
            reactions.sayRandom(
                "Я такого города не знаю."
            )
            reactions.go("/giveCountry")
        }
    }
}

