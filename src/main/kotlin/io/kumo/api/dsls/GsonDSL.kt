package io.kumo.api.dsls

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive

class GsonObject {

    internal val original: JsonObject = JsonObject()

    infix fun String.eq(value: String) {
        original.addProperty(this, value)
    }

    infix fun String.eq(value: Number) {
        original.addProperty(this, value)
    }

    infix fun String.eq(value: Boolean) {
        original.addProperty(this, value)
    }

    infix fun String.eq(value: Char) {
        original.addProperty(this, value)
    }

    infix fun String.eq(value: GsonObject.() -> Unit) {
        original.add(this, GsonObject().apply(value).original)
    }

    infix fun String.eq(value: JsonElement) {
        original.add(this, value)
    }

    infix fun String.eq(value: Array<*>) {
        val array = JsonArray()
        for (item in value) {
            when (item) {
                is String -> {
                    array.add(item)
                }
                is Number -> {
                    array.add(item)
                }
                is Boolean -> {
                    array.add(item)
                }
                is Char -> {
                    array.add(item)
                }
                is JsonElement -> {
                    array.add(item)
                }
            }
        }
        original.add(this, array)
    }

}

fun JsonObject(content: GsonObject.() -> Unit): JsonObject {
    return GsonObject().apply(content).original
}