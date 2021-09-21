package com.justai.jaicf.template.scenario

import com.justai.jaicf.BotEngine
import com.justai.jaicf.activator.caila.CailaIntentActivator
import com.justai.jaicf.activator.caila.caila
import com.justai.jaicf.builder.Scenario
import com.justai.jaicf.channel.ConsoleChannel
import com.justai.jaicf.template.configuration.Configuration

val LoanScenario = Scenario {
    state("AskForLoan") {
        activators {
            intent("AskForLoan")
        }
        action(caila) {
            val number = activator.slots["number"]?.toIntOrNull()
            if (number == null) {
                reactions.say("How much money do you need?")
                return@action
            } else {
                context.session["loanNumber"] = number
            }
            val currency = activator.slots["currency"]
            if (currency == null) {
                reactions.say("Which currency would you like to get?")
                return@action
            } else {
                context.session["loanCurrency"] = currency
            }


            giveLoan(currency, number)
        }

        state("FillSlots") {
            activators {
                intent("CurrencyOrNumber")
            }
            action(caila) {
                val number = activator.slots["number"]?.toIntOrNull() ?: context.session["loanNumber"]
                if (number == null) {
                    reactions.say("How much money do you need?")
                } else {
                    context.session["loanNumber"] = number
                }

                val currency = activator.slots["currency"] ?: context.session["loanCurrency"]
                if (currency == null) {
                    reactions.say("Which currency would you like to get?")
                } else {
                    context.session["loanCurrency"] = currency
                }

                if (currency != null && number != null) {
                    giveLoan(currency as String, number as Int)
                }

            }
        }

        append(YesOrNoScenario)
    }
}

fun main(args: Array<String>) {
    ConsoleChannel(BotEngine(LoanScenario,
        activators = arrayOf(CailaIntentActivator.Factory(Configuration.caila)))).run()
}