package com.prodmobile.template.core.util

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json

// https://medium.com/mercadona-tech/type-safety-in-navigation-compose-23c03e3d74a5
inline fun <reified T : Any> serializableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json { explicitNulls = false }
) = object : NavType<T?>(isNullableAllowed = isNullableAllowed) {

    override fun get(bundle: Bundle, key: String): T? {
        val value = bundle.getString(key)
        return value?.takeIf { it != "null" }?.let { json.decodeFromString<T>(it) }
    }

    override fun parseValue(value: String): T? =
        value.takeIf { it != "null" }?.let { json.decodeFromString<T>(it) }

    override fun serializeAsValue(value: T?): String =
        value?.let { json.encodeToString(it) } ?: "null"

    override fun put(bundle: Bundle, key: String, value: T?) {
        bundle.putString(key, serializeAsValue(value))
    }
}
