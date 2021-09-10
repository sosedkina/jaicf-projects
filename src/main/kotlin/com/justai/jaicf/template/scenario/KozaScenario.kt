package com.justai.jaicf.template.scenario

import com.justai.jaicf.builder.Scenario

val MyKozaScenario = Scenario { 
    state("BuyKoza") {
        activators {
            intent("BuyKoza")
        }
        action {
            reactions.say("I'm in state BuyKoza")
        }
    }
}
