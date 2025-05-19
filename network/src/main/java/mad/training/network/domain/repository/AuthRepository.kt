package mad.training.network.domain.repository

import io.reactivex.rxjava3.core.Single
import mad.training.network.model.error.NetworkError
import mad.training.network.model.login.LoginRequest
import mad.training.network.model.login.LoginResponse
import mad.training.network.model.profile.CreateProfile
import mad.training.network.model.profile.OtpResponse
import mad.training.network.util.ApiResult


interface AuthRepository {
    fun login(request: LoginRequest): Single<ApiResult<LoginResponse>>
    fun createProfileAndOtp(request: CreateProfile): Single<Result<OtpResponse, NetworkError>>
    fun logout(): Single<Result<Unit, NetworkError>>
}