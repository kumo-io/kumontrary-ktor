package io.kumo.api.data

import com.google.gson.JsonObject
import io.kumo.api.dsls.JsonObject
import org.ktorm.entity.Entity

interface User : Entity<User> {

    companion object : Entity.Factory<User>()

    val uid: Int
    val id: String
    val username: String
    val registeredDate: String

    fun toGson(): JsonObject {
        return JsonObject {
            "uid" eq uid
            "id" eq id
            "username" eq username
            "register_date" eq registeredDate
        }
    }

}