package com.prodmobile.template.core.result

sealed interface ResponseResult<out D, out E : ResponseError> {
    data class Success<out D>(val data: D) : ResponseResult<D, Nothing>
    data class Error<out E : ResponseError>(val error: E) :
        ResponseResult<Nothing, E>
}

inline fun <T, E : ResponseError, R> ResponseResult<T, E>.map(map: (T) -> R): ResponseResult<R, E> {
    return when (this) {
        is ResponseResult.Error -> ResponseResult.Error(error)
        is ResponseResult.Success -> ResponseResult.Success(map(data))
    }
}

inline fun <T, E : ResponseError> ResponseResult<T, E>.onSuccess(action: (T) -> Unit): ResponseResult<T, E> {
    return when (this) {
        is ResponseResult.Error -> this
        is ResponseResult.Success -> {
            action(data)
            this
        }
    }
}

inline fun <T, E : ResponseError> ResponseResult<T, E>.onError(action: (E) -> Unit): ResponseResult<T, E> {
    return when (this) {
        is ResponseResult.Error -> {
            action(error)
            this
        }

        is ResponseResult.Success -> this
    }
}

fun <T, E : ResponseError> ResponseResult<T, E>.getOrNull(): T? {
    return when (this) {
        is ResponseResult.Error -> {
            null
        }

        is ResponseResult.Success -> data
    }
}

fun <T, E : ResponseError> ResponseResult<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map { }
}

typealias EmptyResult<E> = ResponseResult<Unit, E>
