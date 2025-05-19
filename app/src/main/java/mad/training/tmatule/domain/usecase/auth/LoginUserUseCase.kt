package mad.training.tmatule.domain.usecase.auth

import android.util.Patterns
import io.reactivex.rxjava3.core.Single
import mad.training.network.domain.repository.AuthRepository
import mad.training.network.model.login.LoginRequest
import mad.training.network.model.login.LoginResponse
import mad.training.network.util.ApiResult
import java.util.regex.Pattern
import kotlin.math.log

class LoginUserUseCase(
    private val repository: AuthRepository
) {

    fun execute(login: LoginRequest): Single<ApiResult<LoginResponse>> {
        val validationError = getValidationError(login)
        if (validationError != null) {
            return Single.just(validationError)
        }
        return repository.login(login)
            .map { result ->
                when (result) {
                    is ApiResult.Error -> ApiResult.Error(
                        result.message,
                        result.type
                    )

                    is ApiResult.Success -> ApiResult.Success(result.data)
                }
            }
    }

    private fun getValidationError(login: LoginRequest): ApiResult.Error? {
        return when {
            !checkEmail(login.email) -> ApiResult.Error(
                message = "Неверный формат почты",
            )

            !checkPassword(login.password) -> ApiResult.Error(
                message = "Неверный формат пароля"
            )

            else -> null
        }
    }

    private fun checkEmail(email: String): Boolean {
        return email.isNotEmpty() && Pattern.matches(
            ("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"), email
        )
    }

    private fun checkPassword(password: String): Boolean {
        return password.isNotEmpty() && password.length >= 8
    }
}