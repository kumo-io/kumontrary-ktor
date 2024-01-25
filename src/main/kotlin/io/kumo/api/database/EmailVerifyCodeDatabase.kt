package io.kumo.api.database

import io.kumo.api.data.EmailVerifyCode
import org.ktorm.schema.Table
import org.ktorm.schema.text
import org.ktorm.schema.timestamp

object EmailVerifyCodeDatabase : Table<EmailVerifyCode>("kumo_emailverifies") {

    val timestamp = timestamp("created_at").primaryKey()

    val email = text("email").bindTo { it.email }
    val verifyCode = text("verify_code").bindTo { it.verifyCode }

}