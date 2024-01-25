package io.kumo.api.routes.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.kumo.api.dsls.*

fun Route.v1() {
    get("status") {
        call.responseGson {
            "code" eq 0
            "message" eq "success"
            "data" eq {
                // 0 for good
                // 1 for maintaining
                // -1 for shutdown
                "status" eq 0
            }
        }
    }

    route("user") {
        user()
    }

    route("auth") {
        auth()
    }

    route("security") {
        security()
    }

}