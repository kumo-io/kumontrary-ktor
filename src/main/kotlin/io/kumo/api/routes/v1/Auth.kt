package io.kumo.api.routes.v1

import com.wusatosi.recaptcha.ErrorCode
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.utils.io.errors.*
import io.kumo.api.KUMO_DATABASE
import io.kumo.api.RECAPTCHA_CLIENT
import io.kumo.api.dsls.responseGson
import io.kumo.api.emailVerifyCodes
import io.kumo.api.utils.EMAIL_REGEX
import org.ktorm.dsl.eq
import org.ktorm.entity.find

fun Route.auth() {
    post("login") {
        val req = call.request

        val parameters = req.queryParameters

        if (!parameters.contains("token")) {
            call.responseGson {
                "code" eq 101
                "message" eq "Required parameters not delivered"
            }
            return@post
        }

        val token = parameters["token"]

        if (token == null) {
            call.responseGson {
                "code" eq -1
                "message" eq "Internal error"
            }
            return@post
        }
    }

    post("register") {
        val req = call.request

        val parameters = req.queryParameters

        if (!parameters.contains("token") || !parameters.contains("email_verify_code") || !parameters.contains("email")) {
            call.responseGson {
                "code" eq 101
                "message" eq "Required parameters not delivered"
            }
            return@post
        }

        val token = parameters["token"]
        val emailVerifyCode = parameters["email_verify_code"]
        val email = parameters["email"]

        if (token == null || emailVerifyCode == null || email == null) {
            call.responseGson {
                "code" eq -1
                "message" eq "Internal error"
            }
            return@post
        }

        if (!EMAIL_REGEX.matches(email)) {
            call.responseGson {
                "code" eq 1004
                "message" eq "Email is invalid"
            }
            return@post
        }

        try {
            val verificationResult = RECAPTCHA_CLIENT.getDetailedResponse(token)
            if (verificationResult.isLeft) {
                if (verificationResult.left == ErrorCode.InvalidToken) {
                    call.responseGson {
                        "code" eq 1001
                        "message" eq "Token is invalid"
                    }
                    return@post
                }
                if (verificationResult.left == ErrorCode.TimeOrDuplicatedToken) {
                    call.responseGson {
                        "code" eq 1002
                        "message" eq "Token is expired or duplicated"
                    }
                    return@post
                }
                call.responseGson {
                    "code" eq -1
                    "message" eq "Internal error"
                }
                return@post
            }

            val responseDetail = verificationResult.right.first

            if (!responseDetail.success) {
                call.responseGson {
                    "code" eq 1003
                    "message" eq "Failed to verify token"
                }
                return@post
            }

            val storedEmailVerifyCode = KUMO_DATABASE.emailVerifyCodes.find {
                it.email eq email.lowercase()
            }

            if (storedEmailVerifyCode == null) {
                call.responseGson {
                    "code" eq 1005
                    "message" eq "No verify code for this email!"
                }
                return@post
            }

            if (storedEmailVerifyCode.verifyCode != emailVerifyCode) {
                call.responseGson {
                    "code" eq 1006
                    "message" eq "Token is not correct"
                }
                return@post
            }

        } catch (e: IOException) {
            call.responseGson {
                "code" eq -2
                "message" eq "IO error"
            }
        }
    }
}