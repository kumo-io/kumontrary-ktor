package io.kumo.api

import com.wusatosi.recaptcha.v3.RecaptchaV3Client
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.kumo.api.database.EmailVerifyCodeDatabase
import io.kumo.api.database.UserDatabase
import io.kumo.api.routes.api
import io.kumo.api.utils.envOrDefault
import io.kumo.api.utils.envOrExit
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf
import org.slf4j.LoggerFactory

val KUMO_LOGGER = LoggerFactory.getLogger("Kumontrary")

lateinit var KUMO_DATABASE: Database

lateinit var RECAPTCHA_CLIENT: RecaptchaV3Client

val Database.users get() = this.sequenceOf(UserDatabase)
val Database.emailVerifyCodes get() = this.sequenceOf(EmailVerifyCodeDatabase)

fun main() {
    KUMO_LOGGER.info("Initializing Kumontrary server...")

    val postgreHost = envOrExit("KUMO_POSTGRE_HOST")
    val postgrePort = envOrExit("KUMO_POSTGRE_PORT").toInt()
    val postgreDatebase = envOrExit("KUMO_POSTGRE_DATABASE")
    val postgreUsername = envOrExit("KUMO_POSTGRE_USERNAME")
    val postgrePassword = envOrExit("KUMO_POSTGRE_PASSWORD")

    val reCaptchaSecret = envOrExit("KUMO_RECAPTCHA_SECRET")

    KUMO_LOGGER.info("Connecting to database...")

    KUMO_DATABASE = Database.connect(
        "jdbc:postgresql://$postgreHost:$postgrePort/$postgreDatebase",
        user = postgreUsername,
        password = postgrePassword
    )

    KUMO_LOGGER.info("Connecting to reCaptcha server...")

    RECAPTCHA_CLIENT = RecaptchaV3Client.create(reCaptchaSecret)

    KUMO_LOGGER.info("Starting Kumontrary server...")

    embeddedServer(
        Netty,
        port = envOrDefault("KUMO_PORT", "8080").toInt(),
        host = envOrDefault("KUMO_HOST", "0.0.0.0"),
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    KUMO_LOGGER.info("Loading Kumontrary modules...")

    install(ContentNegotiation) {
        gson {
        }
    }
    api()

    KUMO_LOGGER.info("Server started!")
}
