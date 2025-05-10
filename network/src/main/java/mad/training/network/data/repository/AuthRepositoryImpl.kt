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

class AuthRepositoryImpl(
    private val api: ApiService
) : AuthRepository {
    override fun login(request: LoginRequest): Single<Result<LoginResponse, NetworkError>> {
        return wrapNetworkRequest(api.login(request))
    }

    override fun createProfileAndOtp(request: CreateProfile): Single<Result<OtpResponse, NetworkError>> {
        return wrapNetworkRequest(api.createProfileAndOtp(request))
    }

    override fun logout(): Single<Result<Unit, NetworkError>> {
        return wrapNetworkRequest(api.logout())
    }
}