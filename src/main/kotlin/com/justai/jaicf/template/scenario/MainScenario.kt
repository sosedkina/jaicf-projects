package com.justai.jaicf.template.scenario

import com.justai.jaicf.builder.Scenario
import com.justai.jaicf.hook.AnyErrorHook
import com.justai.jaicf.reactions.buttons
import com.justai.jaicf.reactions.toState
import com.justai.jaicf.template.configuration.Configuration
import io.ktor.util.*

val MainScenario = Scenario {

    append(MainGameScenario)

    handle<AnyErrorHook> {
        reactions.say(Configuration.bot.onErrorReply)
        logger.error(exception)
    }

    state("start") {
        activators {
            regex("/start")
            intent("Hello")
        }
        action {
            reactions.run {
                image("https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/Flag-map_of_the_world_%282018%29.png/1209px-Flag-map_of_the_world_%282018%29.png")
                sayRandom(
                    "Привет! Давай поиграем? Я называю страну, а ты её столицу!",
                    "Привет! Хочешь, я буду загадывать тебе страны, а ты угадывать столицы?"
                )
                buttons(
                    "Начать игру" toState "/game"
                )
            }
        }
    }


    state("bye") {
        activators {
            intent("Bye")
            intent("StopGame")
        }

        action {
            reactions.sayRandom(
                "Жду тебя снова в игре!",
                "Приходи поиграть еще!"
            )
            reactions.image("https://kartinki.org/uploads/posts/2017-08/1503421241_2283-ya-zhdu-tebya-vozvraschaysya-pobystree.gif")
        }
    }

    fallback {
        reactions.sayRandom(
            "Прости, я тебя не понимаю.",
            "Попробуй сказать иначе."
        )
    }
}