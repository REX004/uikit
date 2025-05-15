package mad.training.network.util

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val type: ErrorType = ErrorType.GENERAL_SERVER_ERROR) :
        ApiResult<Nothing>()
}

enum class ErrorType {
    NO_INTERNET,
    GENERAL_SERVER_ERROR,
}