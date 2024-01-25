package io.kumo.api.routes.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.kumo.api.KUMO_DATABASE
import io.kumo.api.data.User
import io.kumo.api.database.UserDatabase
import io.kumo.api.dsls.responseGson
import io.kumo.api.users
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.ktorm.entity.find

fun Route.user() {
    get {
        val req = call.request

        val parameters = req.queryParameters

        if (!parameters.contains("id") and !parameters.contains("uid")) {
            call.responseGson {
                "code" eq 101
                "message" eq "Required parameters not delivered"
            }
            return@get
        }

        if (parameters.contains("uid")) {
            val uid = parameters["uid"]

            if (uid == null) {
                if (parameters.contains("id")) {
                    val id = parameters["id"]
                    if (id == null) {
                        call.responseGson {
                            "code" eq -1
                            "message" eq "Internal error"
                        }
                        return@get
                    }
                    val user = tryFindUserById(id)

                    if (user == null) {
                        call.responseGson {
                            "code" eq 8001
                            "message" eq "User with this id not exists"
                        }
                        return@get
                    }
                    call.responseGson {
                        "code" eq 0
                        "message" eq "success"
                        "data" eq user.toGson()
                    }
                    return@get
                } else {
                    call.responseGson {
                        "code" eq -1
                        "message" eq "Internal error"
                    }
                    return@get
                }
            } else {
                val user = tryFindUserByUid(uid)

                if (user == null) {
                    call.responseGson {
                        "code" eq 8001
                        "message" eq "User with this uid not exists"
                    }
                    return@get
                }
                call.responseGson {
                    "code" eq 0
                    "message" eq "success"
                    "data" eq user.toGson()
                }
                return@get
            }
        }

        if (parameters.contains("id")) {
            val id = parameters["id"]

            if (id == null) {
                call.responseGson {
                    "code" eq -1
                    "message" eq "Internal error"
                }
            } else {
                val user = tryFindUserById(id)

                if (user == null) {
                    call.responseGson {
                        "code" eq 8001
                        "message" eq "User with this id not exists"
                    }
                    return@get
                }
                call.responseGson {
                    "code" eq 0
                    "message" eq "success"
                    "data" eq user.toGson()
                }
                return@get
            }
        }

        call.responseGson {
            "code" eq -1
            "message" eq "Internal error"
        }
    }
}

private fun tryFindUserByUid(uid: String): User? {
    return KUMO_DATABASE.users.find {
        it.uid eq uid.toInt()
    }
}

private fun tryFindUserById(id: String): User? {
    return KUMO_DATABASE.users.find {
        it.id eq id
    }
}