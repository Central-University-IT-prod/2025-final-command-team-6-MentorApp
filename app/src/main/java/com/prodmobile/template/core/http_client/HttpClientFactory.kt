package com.prodmobile.template.core.http_client

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

object HttpClientFactory {
    @OptIn(ExperimentalSerializationApi::class)
    fun create(): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(json = Json {
                    decodeEnumsCaseInsensitive = true
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
            }

            install(HttpTimeout)
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                    }
                }
                level = LogLevel.NONE
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }
}
