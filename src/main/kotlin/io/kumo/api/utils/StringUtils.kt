package io.kumo.api.utils

import io.kumo.api.KUMO_LOGGER
import kotlin.system.exitProcess

val EMAIL_REGEX = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$".toRegex()

fun env(key: String): String {
    return System.getenv(key)
}

fun envOrDefault(key: String, defaultValue: String): String {
    return System.getenv(key) ?: defaultValue
}

fun envOrExit(key: String): String {
    val value = System.getenv(key)
    if (value == null) {
        KUMO_LOGGER.error("The required environment variable \"$key\" not exists!")
        exitProcess(-1)
    }
    return System.getenv(key)
}