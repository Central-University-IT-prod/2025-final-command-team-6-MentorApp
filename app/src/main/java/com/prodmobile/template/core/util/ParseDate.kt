package com.prodmobile.template.core.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatDate(dateString: String): String {
    return try {
        val instant = Instant.parse(dateString)
        val formatter = DateTimeFormatter
            .ofPattern("dd MMM yyyy, HH:mm")
            .withLocale(Locale("ru"))
            .withZone(ZoneId.systemDefault())

        formatter.format(instant)
    } catch (e: Exception) {
        "Неверный формат даты"
    }
}