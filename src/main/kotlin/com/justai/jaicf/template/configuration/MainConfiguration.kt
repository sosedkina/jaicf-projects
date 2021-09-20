package com.justai.jaicf.template.configuration

import com.justai.jaicf.activator.caila.CailaNLUSettings
import com.justai.jaicf.activator.caila.CailaTypeToken
import com.sksamuel.hoplite.ConfigLoader
import com.sksamuel.hoplite.PropertySource

private val ConfigurationLoader = ConfigLoader.Builder()
    .addSource(PropertySource.resource("/application-local.yml", optional = true))
    .addSource(PropertySource.resource("/application.yml", optional = false))
    .build()

data class MainConfiguration(
    val caila: CailaNLUSettings,
    val connection: ConnectionsConfiguration,
    val bot: BotConfiguration,
)

data class ConnectionsConfiguration(
    val mode: ConnectionMode,
    val accessToken: String,
) {
    enum class ConnectionMode { polling, webhook }
}

data class BotConfiguration(
    val onErrorReply: String,
    val operatorNumber: String
)


val Configuration: MainConfiguration = ConfigurationLoader.loadConfigOrThrow()
