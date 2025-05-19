package mad.training.network.data.repository

import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import mad.training.network.domain.repository.Result
import mad.training.network.model.error.NetworkError
import retrofit2.HttpException
import retrofit2.Response as RetrofitResponse
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * Оборачивает сетевой вызов Retrofit (возвращающий Single<RetrofitResponse<T>>)
 * в Single<Result<T, NetworkError>>.
 *
 * @param T Тип успешного ответа (тело ответа). Должен быть Any, так как Unit тоже обрабатывается.
 * @param apiCall Лямбда, возвращающая Single<RetrofitResponse<T>> от ApiService.
 * @return Single, который эммитит Result.Success с данными типа T или Result.Error с NetworkError.
 */
fun <T : Any> wrapNetworkRequest(
    apiCall: Single<RetrofitResponse<T>>
): Single<Result<T, NetworkError>> {
    return apiCall
        .map { response: RetrofitResponse<T> ->
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    // Успешный ответ с телом
                    Result.Success(body)
                } else {
                    // Успешный ответ (например, 204 No Content), но тело пустое.
                    // Проверяем, ожидался ли Unit.
                    if (response.code() == 204) { // Если T это Unit
                        @Suppress("UNCHECKED_CAST")
                        Result.Success(Unit as T)
                    } else {
                        // Тело null, но ожидался не Unit - это ошибка сериализации или контракта API.
                        val causeException =
                            NullPointerException("Response body is null, but type T was not Unit.")
                        Result.Error(
                            NetworkError.SerializationError(
                                errorMessage = causeException.message,
                                cause = causeException
                            )
                        )
                    }
                }
            } else {
                // HTTP ошибка (4xx, 5xx)
                val errorBodyString = try {
                    response.errorBody()?.string() // Читаем тело ошибки ОДИН РАЗ
                } catch (e: Exception) {
                    // Если чтение errorBody вызывает ошибку, используем стандартное сообщение
                    null
                }

                Result.Error(
                    NetworkError.HttpError(
                        httpCode = response.code(),
                        serverMessage = if (errorBodyString.isNullOrBlank()) response.message() else errorBodyString,
                        cause = HttpException(response) // Передаем HttpException с полным объектом Response
                    )
                )
            }
        }
        .onErrorReturn { throwable: Throwable ->
            // Обработка исключений, которые произошли до или во время выполнения запроса,
            // или при обработке ответа (например, ошибка десериализации).
            when (throwable) {
                is SocketTimeoutException -> Result.Error(NetworkError.Timeout(cause = throwable))
                is IOException -> {
                    // ConnectivityInterceptor должен был выбросить IOException с сообщением "Нет подключения к интернету".
                    Result.Error(NetworkError.NoInternetConnection(cause = throwable))
                }

                is HttpException -> {
                    // Эта ветка сработает, если Retrofit сам выбросит HttpException
                    // (например, если ApiService возвращает Single<T> без Response<T>,
                    // или ошибка в адаптере RxJava до формирования Response).
                    Result.Error(
                        NetworkError.HttpError(
                            httpCode = throwable.code(),
                            serverMessage = throwable.message(),
                            cause = throwable
                        )
                    )
                }

                is JsonSyntaxException, is JsonParseException -> {
                    // Явные ошибки парсинга JSON
                    Result.Error(
                        NetworkError.SerializationError(
                            errorMessage = throwable.localizedMessage ?: "Ошибка парсинга JSON",
                            cause = throwable
                        )
                    )
                }

                else -> {
                    // Все остальные ошибки, включая потенциальные ошибки десериализации,
                    // если они не были пойманы ранее или являются другими RuntimeException.
                    Result.Error(
                        NetworkError.Unknown(
                            detailedMessage = throwable.localizedMessage,
                            cause = throwable
                        )
                    )
                    // Альтернативно, можно все неизвестные тоже считать SerializationError, если это наиболее частый случай
                    // Result.Error(NetworkError.SerializationError(errorMessage = throwable.localizedMessage, cause = throwable))
                }
            }
        }
}

/**
 * Вспомогательная функция для оборачивания вызовов, которые возвращают Completable (например, logout,
 * если он не возвращает тело ответа, а только статус успеха/ошибки).
 */
fun wrapCompletableRequest(
    apiCall: Completable
): Single<Result<Unit, NetworkError>> {
    return apiCall
        .toSingleDefault(Result.Success(Unit) as Result<Unit, NetworkError>) // Преобразуем успешный Completable в Single<Result.Success<Unit>>
        .onErrorReturn { throwable ->
            // Логика обработки ошибок идентична той, что в wrapNetworkRequest
            when (throwable) {
                is SocketTimeoutException -> Result.Error(NetworkError.Timeout(cause = throwable))
                is IOException -> Result.Error(NetworkError.NoInternetConnection(cause = throwable))
                is HttpException -> Result.Error(
                    NetworkError.HttpError(
                        httpCode = throwable.code(),
                        serverMessage = throwable.message(),
                        cause = throwable
                    )
                )

                is JsonSyntaxException, is JsonParseException -> {
                    Result.Error(
                        NetworkError.SerializationError(
                            errorMessage = throwable.localizedMessage ?: "Ошибка парсинга JSON",
                            cause = throwable
                        )
                    )
                }

                else -> Result.Error(
                    NetworkError.Unknown(
                        detailedMessage = throwable.localizedMessage,
                        cause = throwable
                    )
                )
            }
        }
}