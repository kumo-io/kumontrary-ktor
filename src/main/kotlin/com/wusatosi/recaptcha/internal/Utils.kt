package com.wusatosi.recaptcha.internal

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.wusatosi.recaptcha.UnexpectedJsonStructure
import java.util.regex.Pattern

private val pattern = Pattern.compile("^[-a-zA-Z0-9+&@#/%?=~_!:,.;]*[-a-zA-Z0-9+&@#/%=~_]")

// There's no way to validate if a site secret/ token is valid,
// the only thing we know is that if it's not URL compatible, it's not a valid token
internal fun likelyValidRecaptchaParameter(target: String): Boolean = pattern.matcher(target).matches()

internal fun JsonElement?.expectStringArray(attributeName: String): List<String> {
    this ?: throwNull(attributeName)
    if (!this.isJsonArray)
        throw UnexpectedJsonStructure(
            "$attributeName attribute is not an array"
        )
    return this.asJsonArray.map { it.expectString(attributeName) }
}

private fun JsonElement?.expectPrimitive(attributeName: String, type: String): JsonPrimitive {
    this ?: throwNull(attributeName)
    if (!this.isJsonPrimitive)
        throw UnexpectedJsonStructure(
            "$attributeName attribute is not a $type"
        )
    return this.asJsonPrimitive
}

internal fun JsonElement?.expectString(attributeName: String): String {
    this ?: throwNull(attributeName)
    if (!this.expectPrimitive(attributeName, "string").isString)
        throw UnexpectedJsonStructure(
            "$attributeName attribute is not an string"
        )
    return this.asString
}

private fun JsonPrimitive.expectBoolean(attributeName: String): Boolean {
    if (!this.isBoolean)
        throw UnexpectedJsonStructure(
            "$attributeName attribute is not a boolean"
        )
    return this.asBoolean
}

internal fun JsonElement?.expectBoolean(attributeName: String) = this
    .expectPrimitive(attributeName, "boolean")
    .expectBoolean(attributeName)

private fun JsonPrimitive.expectNumber(attributeName: String): JsonPrimitive {
    if (!this.isNumber)
        throw UnexpectedJsonStructure(
            "$attributeName attribute is not an number"
        )
    return this
}

internal fun JsonElement?.expectNumber(attributeName: String): Double = this
    .expectPrimitive(attributeName, "number")
    .expectNumber(attributeName)
    .asDouble

private fun throwNull(attributeName: String): Nothing {
    throw UnexpectedJsonStructure(
        "$attributeName do not exists"
    )
}
