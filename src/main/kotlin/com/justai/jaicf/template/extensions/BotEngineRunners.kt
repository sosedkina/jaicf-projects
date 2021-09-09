package com.justai.jaicf.template.extensions

import com.justai.jaicf.BotEngine
import com.justai.jaicf.channel.jaicp.JaicpChannelFactory
import com.justai.jaicf.channel.jaicp.JaicpPollingConnector
import com.justai.jaicf.channel.jaicp.JaicpServer
import com.justai.jaicf.template.configuration.Configuration
import com.justai.jaicf.template.configuration.ConnectionsConfiguration

fun BotEngine.run(vararg channels: JaicpChannelFactory) = when (Configuration.connection.mode) {
    ConnectionsConfiguration.ConnectionMode.polling -> {
        JaicpPollingConnector(this, Configuration.connection.accessToken, channels = channels.toList()).runBlocking()
    }
    ConnectionsConfiguration.ConnectionMode.webhook -> {
        JaicpServer(this, Configuration.connection.accessToken, channels = channels.toList()).start(wait = true)
    }
}