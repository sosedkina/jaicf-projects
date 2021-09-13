package com.justai.jaicf.template

import com.justai.jaicf.BotEngine
import com.justai.jaicf.activator.caila.CailaIntentActivator
import com.justai.jaicf.activator.regex.RegexActivator
import com.justai.jaicf.channel.jaicp.channels.ChatWidgetChannel
import com.justai.jaicf.channel.jaicp.channels.TelephonyChannel
import com.justai.jaicf.channel.jaicp.logging.JaicpConversationLogger
import com.justai.jaicf.logging.Slf4jConversationLogger
import com.justai.jaicf.template.configuration.Configuration
import com.justai.jaicf.template.extensions.run
import com.justai.jaicf.template.scenario.TelephonyScenario

val TelephonyBot = BotEngine(
    scenario = TelephonyScenario,
    conversationLoggers = arrayOf(
        JaicpConversationLogger(Configuration.connection.accessToken),
        Slf4jConversationLogger()
    ),
    activators = arrayOf(
        CailaIntentActivator.Factory(Configuration.caila),
        RegexActivator
    )
)

fun main() {
    TelephonyBot.run(ChatWidgetChannel, TelephonyChannel)
}