package io.kumo.api.dsls

import io.ktor.server.application.*
import io.ktor.server.response.*

suspend fun ApplicationCall.responseGson(content: GsonObject.() -> Unit) {
    this.respond(JsonObject(content))
}