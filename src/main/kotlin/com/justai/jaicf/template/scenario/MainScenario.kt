package com.justai.jaicf.template.scenario

import com.justai.jaicf.activator.caila.caila
import com.justai.jaicf.builder.Scenario
import com.justai.jaicf.channel.jaicp.channels.TelephonyEvents
import com.justai.jaicf.channel.jaicp.dto.jaicpAnalytics
import com.justai.jaicf.channel.jaicp.reactions.chatwidget
import com.justai.jaicf.channel.jaicp.reactions.telephony
import com.justai.jaicf.channel.jaicp.telephony
import com.justai.jaicf.helpers.logging.logger
import com.justai.jaicf.hook.AnyErrorHook
import com.justai.jaicf.template.configuration.Configuration
import io.ktor.util.*

val TelephonyScenario = Scenario {

    handle<AnyErrorHook> {
        reactions.say(Configuration.bot.onErrorReply)
        logger.error(exception)
    }

    state("ringing") {
        activators {
            event(TelephonyEvents.RINGING)
        }
        action(telephony) {
            logger.debug("Incoming call from ${request.caller}")
        }
    }

    state("start") {
        activators {
            regex("/start")
        }
        action {
            val answer = "Hello! Thanks for calling My Example Service. How can I help you?"
            reactions.telephony?.say(answer, bargeIn = true)
            reactions.chatwidget?.say(answer)
        }
    }

    state("AskForLoan") {
        activators {
            intent("AskForLoan")
        }
        action {
            reactions.say("How much money do you need?")
        }
        
        state("LoanAmount") {
            activators {
                intent("LoanAmount")
            }
            action(caila) {
                val amount = activator.slots["amount"] as String
                reactions.say("Okay! I will lend you $amount dollars.")
                jaicpAnalytics.setSessionResult("")
//                jaicpAnalytics.setMessageLabel("")
            }
        }
    }

    state("bye") {
        activators {
            intent("Bye")
        }
        action {
            reactions.say("Ok!")
        }
    }

    state("later") {
        activators {
            intent("CallMeLater")
        }
        action {
            reactions.say("Ok!")
        }
    }

    state("smalltalk", noContext = true) {
        activators {
            anyIntent()
        }

        action(caila) {
            activator.topIntent.answer?.let { reactions.say(it) } ?: reactions.go("/fallback")
        }
    }

    fallback {
        reactions.sayRandom(
            "Sorry, I didn't get that...",
            "Sorry, could you repeat please?"
        )
    }
}