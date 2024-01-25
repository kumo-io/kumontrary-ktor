package io.kumo.api.data

import org.ktorm.entity.Entity

interface EmailVerifyCode : Entity<EmailVerifyCode> {

    companion object : Entity.Factory<EmailVerifyCode>()

    val email: String
    val verifyCode: String

}