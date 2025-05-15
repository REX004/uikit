package mad.training.network.util

import retrofit2.HttpException
import java.io.IOException

object NetworkErrorHandler {

    fun handleThrowable(throwable: Throwable): ApiResult.Error {
        return when (throwable) {
            is IOException -> {
                if (throwable.message?.contains(
                        "Нет подключения к интернету",
                        ignoreCase = true
                    ) == true
                ) {
                    ApiResult.Error(
                        "Отсутствует подключение к интернету. Проверьте соединение.",
                        ErrorType.NO_INTERNET
                    )
                } else {
                    ApiResult.Error(
                        throwable.message ?: "Ошибка сети или ввода-вывода",
                        ErrorType.GENERAL_SERVER_ERROR
                    )
                }
            }

            is HttpException -> {
                ApiResult.Error(
                    "Ошибка сервера: ${throwable.code()} ${throwable.message()}",
                    ErrorType.GENERAL_SERVER_ERROR
                )
            }

            else -> ApiResult.Error(
                throwable.message ?: "Неизвестная ошибка",
                ErrorType.GENERAL_SERVER_ERROR
            )
        }
    }

}