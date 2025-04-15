package com.prodmobile.template.core.http_client

import com.prodmobile.template.core.result.NetworkError
import com.prodmobile.template.core.result.ResponseResult
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.ensureActive
import java.net.SocketTimeoutException
import java.nio.channels.UnresolvedAddressException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): ResponseResult<T, NetworkError> {
    val response = try {
        execute()
    } catch (e: SocketTimeoutException) {
        return ResponseResult.Error(NetworkError.RequestTimeout)
    } catch (e: UnresolvedAddressException) {
        return ResponseResult.Error(NetworkError.NoInternet)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        return ResponseResult.Error(NetworkError.Unknown)
    }

    return responseToResult(response)
}

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): ResponseResult<T, NetworkError> {
    return when (response.status.value) {
        in 200..299 -> {
            try {
                ResponseResult.Success(response.body<T>())
            } catch (e: NoTransformationFoundException) {
                ResponseResult.Error(NetworkError.Serialization)
            }
        }

        401 -> ResponseResult.Error(NetworkError.Unauthorized)
        403 -> ResponseResult.Error(NetworkError.InvalidUsernameOrPassword)
        408 -> ResponseResult.Error(NetworkError.RequestTimeout)
        429 -> ResponseResult.Error(NetworkError.TooManyRequests)
        in 500..599 -> ResponseResult.Error(NetworkError.Server)
        else -> ResponseResult.Error(NetworkError.Custom(response.body<String>().toString()))
    }
}
