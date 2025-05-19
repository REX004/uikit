package mad.training.network.model.error

import java.io.IOException

sealed class NetworkError(
    open val displayMessage: String? = null,
    open val cause: Throwable? = null
) {

    data class NoInternetConnection(
        override val displayMessage: String = "Отсутствует подключение к интернету. Пожалуйста, проверьте ваше соединение.",
        override val cause: IOException? = null // Обычно IOException от интерцептора
    ) : NetworkError(displayMessage, cause)

    /**
     * Ошибка HTTP-запроса (например, 4xx, 5xx).
     * @param httpCode Код состояния HTTP (например, 401, 404, 500).
     * @param serverMessage Сообщение от сервера, если оно было в теле ответа об ошибке или в заголовках.
     *                      Может быть null.
     * @param customDisplayMessage Заготовленное сообщение для пользователя, если serverMessage не подходит.
     */
    data class HttpError(
        val httpCode: Int,
        val serverMessage: String? = null, // Сообщение из errorBody или response.message()
        val customDisplayMessage: String? = null, // Если хотим свое сообщение для определенных кодов
        override val cause: Throwable? = null // Обычно HttpException
    ) : NetworkError(
        customDisplayMessage ?: serverMessage ?: "Ошибка сервера (код: $httpCode)",
        cause
    ) {
        // Дополнительные удобные проверки
        fun isUnauthorized(): Boolean = httpCode == 401
        fun isForbidden(): Boolean = httpCode == 403
        fun isNotFound(): Boolean = httpCode == 404
        fun isClientError(): Boolean = httpCode in 400..499
        fun isServerError(): Boolean = httpCode in 500..599
    }

    /**
     * Ошибка: Превышено время ожидания ответа от сервера.
     */
    data class Timeout(
        override val displayMessage: String = "Превышено время ожидания ответа от сервера. Попробуйте позже.",
        override val cause: java.net.SocketTimeoutException? = null
    ) : NetworkError(displayMessage, cause)

    /**
     * Ошибка сериализации/десериализации данных (например, при парсинге JSON).
     * @param errorMessage Описание ошибки сериализации.
     */
    data class SerializationError(
        val errorMessage: String? = null,
        override val cause: Throwable? = null // Например, JsonSyntaxException, NullPointerException если тело null где не ожидалось
    ) : NetworkError(
        errorMessage ?: "Ошибка обработки данных. Пожалуйста, попробуйте позже.",
        cause
    )

    /**
     * Неизвестная или необработанная ошибка.
     * @param detailedMessage Детальное сообщение об ошибке для логирования.
     */
    data class Unknown(
        val detailedMessage: String? = null,
        override val displayMessage: String = "Произошла неизвестная ошибка. Пожалуйста, попробуйте еще раз.",
        override val cause: Throwable? = null
    ) : NetworkError(displayMessage, cause)

    /**
     * Опционально: Специфическая ошибка API, если сервер возвращает структурированные ошибки.
     * @param apiErrorCode Пользовательский код ошибки от вашего бэкенда.
     * @param apiMessage Сообщение, связанное с этим кодом ошибки.
     */
    data class SpecificApiError(
        val apiErrorCode: String,
        val apiMessage: String,
        override val cause: Throwable? = null
    ) : NetworkError(apiMessage, cause)


    /**
     * Возвращает сообщение, подходящее для отображения пользователю.
     * Если displayMessage был предоставлен при создании, используется он.
     * Иначе, используется сообщение по умолчанию для данного типа ошибки.
     */
    fun getUserFriendlyMessage(): String {
        return displayMessage ?: when (this) {
            is NoInternetConnection -> "Отсутствует подключение к интернету. Пожалуйста, проверьте ваше соединение."
            is HttpError -> customDisplayMessage ?: serverMessage
            ?: "Ошибка сервера (код: $httpCode). Попробуйте позже."

            is Timeout -> "Превышено время ожидания ответа от сервера. Попробуйте позже."
            is SerializationError -> "Ошибка обработки полученных данных. Пожалуйста, попробуйте позже."
            is Unknown -> "Произошла неизвестная ошибка. Пожалуйста, попробуйте еще раз."
            is SpecificApiError -> apiMessage
        }
    }
}