package com.justai.jaicf.template.contextInfo

import com.justai.jaicf.context.BotContext

class GameInfo (context: BotContext) {

    var countryGiven: String? by context.client
    var cityGiven: String? by context.client
}