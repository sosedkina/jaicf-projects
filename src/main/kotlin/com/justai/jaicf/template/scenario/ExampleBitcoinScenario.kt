package com.justai.jaicf.template.scenario

import com.justai.jaicf.builder.Scenario
import com.justai.jaicf.template.http.httpClient
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking

data class BitcoinToUSD(val USD: Record) {
    data class Record(val buy: Double, val sell: Double)
}

val ExampleBitcoinScenario = Scenario {
    state("game") {
        action {
            reactions.say("Поехали!")
            reactions.go("/giveCountry")
        }
    }

    state("giveCountry") {
        action {
            reactions.say("Какой город является столицей Великобритании?")
        }


        state("cityGiven") {
            activators {
                intent("City")
            }

            action {
                reactions.say("Ты назвал город, это уже не плохо. Но я пока не знаю, правильный ли это ответ.")
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

