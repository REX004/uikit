package mad.training.network.data.repository

import io.reactivex.rxjava3.core.Single
import mad.training.network.data.remote.ApiService
import mad.training.network.domain.repository.AuthRepository
import mad.training.network.domain.repository.Result
import mad.training.network.model.error.NetworkError
import mad.training.network.model.login.LoginRequest
import mad.training.network.model.login.LoginResponse
import mad.training.network.model.profile.CreateProfile
import mad.training.network.model.profile.OtpResponse
import mad.training.network.util.ApiResult
import mad.training.network.util.NetworkErrorHandler

class AuthRepositoryImpl(
    private val api: ApiService
) : AuthRepository {
    override fun login(request: LoginRequest): Single<ApiResult<LoginResponse>> {
        return api.login(request.email, request.password)
            .map<ApiResult<LoginResponse>> { authResponse ->
                if (authResponse.isNotEmpty()) {
                    ApiResult.Success(authResponse[0])
                } else {
                    ApiResult.Error(message = "Пользователь не найден или неверные данные")
                }
            }
            .onErrorReturn { throwable ->
                NetworkErrorHandler.handleThrowable(throwable)
            }
    }

    override fun createProfileAndOtp(request: CreateProfile): Single<Result<OtpResponse, NetworkError>> {
        return wrapNetworkRequest(api.createProfileAndOtp(request))
    }

    override fun logout(): Single<Result<Unit, NetworkError>> {
        return wrapNetworkRequest(api.logout())
    }
}