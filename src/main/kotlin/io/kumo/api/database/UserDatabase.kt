package io.kumo.api.database

import io.kumo.api.data.User
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.text

object UserDatabase : Table<User>("kumo_users") {

    val uid = int("uid").primaryKey().bindTo { it.uid }

    val id = text("id").bindTo { it.id }
    val username = text("username").bindTo { it.username }
    val registeredDate = text("registered_date").bindTo { it.registeredDate }

}