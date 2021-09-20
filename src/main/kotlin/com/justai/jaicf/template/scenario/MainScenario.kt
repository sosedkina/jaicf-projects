package com.justai.jaicf.template.scenario

import com.justai.jaicf.activator.caila.caila
import com.justai.jaicf.builder.Scenario
import com.justai.jaicf.channel.jaicp.channels.TelephonyEvents
import com.justai.jaicf.channel.jaicp.dto.TelephonySwitchReply
import com.justai.jaicf.channel.jaicp.dto.jaicpAnalytics
import com.justai.jaicf.channel.jaicp.telephony
import com.justai.jaicf.helpers.logging.logger
import com.justai.jaicf.hook.AnyErrorHook
import com.justai.jaicf.template.configuration.Configuration
import com.justai.jaicf.template.nlp.SentimentAnalyzer
import io.ktor.util.*
import java.time.Instant


val TelephonyScenario = Scenario(telephony) {

    handle<AnyErrorHook> {
        reactions.say(Configuration.bot.onErrorReply)
        logger.error(exception)
    }

    state("ringing") {
        activators {
            event(TelephonyEvents.RINGING)
        }
        action(telephony) {
            logger.debug("Outgoing call to ${request.caller}")
        }
    }

    state("start") {
        activators {
            regex("/start")
        }
        action {
            reactions.say(
                text = "Hello! My name is JayCee and i'm a voice assistant for your preferred bank. How can I help you?",
                bargeIn = true
            )
            reactions.startNewSession()
        }
    }

    state("AskForLoan") {
        activators {
            intent("AskForLoan")
        }
        action(caila) {
            val amount = activator.slots["amount"] as String
            reactions.say("Okay! I will lend you $amount. Check your bank account in 5 minutes. Is there any other problem?")
            jaicpAnalytics.setSessionResult("Loan given")
        }

        append(YesOrNoScenario)
    }

    state("WhatCanYouDo") {
        activators {
            intent("WhatCanYouDo")
        }
        action {
            reactions.say("""
                As I said, I'm JayCee, an example of telephone virtual assistant. 
                I was made by Just AI to demonstrate call bot scenarios. 
                This message was intentionally made very lengthy because I want to show you how can I handle interruptions. 
                Feel free to interrupt me anytime you get bored - ask for a loan or say "shut up". If you don't do that I will continue to repeat this message on and on until you run out of patience.
                
                A robot may not injure a human being or, through inaction, allow a human being to come to harm. 
                A robot must obey the orders given it by human beings except where such orders would conflict with the First Law.
                A robot must protect its own existence as long as such protection does not conflict with the First or Second Laws.
                
                A robot may not injure a human being or, through inaction, allow a human being to come to harm. 
                A robot must obey the orders given it by human beings except where such orders would conflict with the First Law.
                A robot must protect its own existence as long as such protection does not conflict with the First or Second Laws. 
            """.trimIndent(), bargeIn = true)
        }
    }

    state("INeedAHuman") {
        activators {
            intent("INeedAHuman")
        }
        action {
            reactions.say("I'll redirect your call to our operator.")
            reactions.transferCall(TelephonySwitchReply(
                phoneNumber = Configuration.bot.operatorNumber,
                continueCall = true,
                continueRecording = true))
        }
    }

    state("later") {
        activators {
            intent("CallMeLater")
        }
        action {
            reactions.say("Ok! I will call you in 25 seconds.")
            reactions.redial(startDateTime = Instant.now().plusSeconds(25))
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

    state("transfer") {
        activators {
            event("transfer")
        }
        action {
            jaicpAnalytics.setSessionResult("Operator helped with result")
            reactions.go("/finish")
        }
    }

    state("finish") {
        activators {
            intent("Bye")
        }

        action {
            reactions.say("Can you rate our services from 1 to 5?")
        }

        state("rate") {
            activators {
                regex("^(0|[1-5])\$")
            }
            action {
                reactions.say("Thanks for calling your preferred bank! Good bye!")
                jaicpAnalytics.setSessionLabel(request.input)
                reactions.hangup()
            }
        }
    }

    state("botHangup") {
        activators {
            event(TelephonyEvents.BOT_HANGUP)
            event(TelephonyEvents.HANGUP)
        }
        action {
            logger.info("Conversation with ${request.caller} finished.")
        }
    }

    state("thanks") {
        activators {
            intent("Thanks")
        }
        action {
            reactions.say("You're welcome! Can I help you with something else?")
        }

        append(YesOrNoScenario)
    }

    fallback {
        jaicpAnalytics.setMessageLabel("fallback", "Unrecognized")
        val sentiment = SentimentAnalyzer.findSentiment(request.input)
        if (sentiment != null && sentiment.intValue < 0) {
            reactions.say("I'll redirect your call to our operator.")
            reactions.transferCall(TelephonySwitchReply(
                phoneNumber = Configuration.bot.operatorNumber,
                continueCall = true)
            )
        } else {
            reactions.say("Sorry, I didn't get that... You can ask me for a loan or who I am. These two topic I can handle so far.")
        }
    }
}