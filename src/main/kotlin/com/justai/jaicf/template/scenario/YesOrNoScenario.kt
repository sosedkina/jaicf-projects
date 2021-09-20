package com.justai.jaicf.template.scenario

import com.justai.jaicf.builder.Scenario
import com.justai.jaicf.channel.jaicp.telephony

val YesOrNoScenario = Scenario(telephony) {
    state("No") {
        activators {
            intent("No")
        }
        action {
            reactions.go("/finish")
        }
    }

    state("Yes") {
        activators {
            intent("Yes")
        }
        action {
            reactions.say("Please, describe your problem.")
        }
    }
}