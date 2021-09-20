package com.justai.jaicf.template.utility

import com.justai.jaicf.BotEngine
import com.justai.jaicf.channel.jaicp.JaicpChannelFactory
import com.justai.jaicf.channel.jaicp.JaicpPollingConnector
import com.justai.jaicf.channel.jaicp.JaicpServer
import com.justai.jaicf.template.configuration.Configuration
import com.justai.jaicf.template.configuration.ConnectionsConfiguration

fun BotEngine.run(vararg channels: JaicpChannelFactory) = when (Configuration.connection.mode) {
    ConnectionsConfiguration.ConnectionMode.polling -> {
        JaicpPollingConnector(
            botApi = this,
            accessToken = Configuration.connection.accessToken,
            channels = channels.toList()
        ).runBlocking()
    }
    ConnectionsConfiguration.ConnectionMode.webhook -> {
        JaicpServer(
            botApi = this,
            accessToken = Configuration.connection.accessToken,
            channels = channels.toList(),
            port = Configuration.connection.port
        ).start(wait = true)
    }
}