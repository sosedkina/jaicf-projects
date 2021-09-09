package com.justai.jaicf.template.serializers

import kotlinx.serialization.json.Json

val Kotlinx = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}
