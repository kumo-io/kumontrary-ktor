package io.kumo.api.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.kumo.api.routes.v1.v1

fun Application.api() {
    routing {
        route("v1") {
            v1()
        }
    }
}