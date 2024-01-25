package io.kumo.api.database

import io.kumo.api.data.ChangePasswordToken
import org.ktorm.schema.Table
import org.ktorm.schema.text
import org.ktorm.schema.timestamp

object ChangePasswordTokenDatabase : Table<ChangePasswordToken>("kumo_registertokens") {

    val timestamp = timestamp("created_at").primaryKey()

    val email = text("email").bindTo { it.email }
    val token = text("token").bindTo { it.token }

}