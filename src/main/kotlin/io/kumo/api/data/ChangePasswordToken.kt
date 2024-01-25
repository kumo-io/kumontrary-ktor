package io.kumo.api.data

import org.ktorm.entity.Entity

interface ChangePasswordToken : Entity<ChangePasswordToken> {

    companion object : Entity.Factory<ChangePasswordToken>()

    val email: String
    val token: String
}