package com.prodmobile.template.core.result

sealed interface NetworkError : ResponseError {
    val message: String

    data object RequestTimeout : NetworkError { override val message = "Request timed out" }
    data object TooManyRequests : NetworkError { override val message = "Too many requests" }
    data object NoInternet : NetworkError { override val message = "No internet connection" }
    data object Server : NetworkError { override val message = "Server error" }
    data object Serialization : NetworkError { override val message = "Serialization error" }
    data object Unknown : NetworkError { override val message = "Unknown error" }
    data object Unauthorized : NetworkError { override val message = "Unauthorized access" }
    data object InvalidUsernameOrPassword : NetworkError { override val message = "Invalid username or password" }
    data class Custom(override val message: String) : NetworkError
}
