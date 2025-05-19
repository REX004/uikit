package mad.training.tmatule.presentation.auth

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import mad.training.network.model.login.LoginRequest
import mad.training.network.model.login.LoginResponse
import mad.training.network.util.ApiResult
import mad.training.network.util.ErrorType
import mad.training.tmatule.domain.usecase.auth.LoginUserUseCase

/**
 * Описание назначения класса: ViewModel для экрана авторизации.
 * Управляет состоянием полей ввода, валидацией и процессом входа.
 * Автор: Темирзянов Амир
 */

sealed class LoginUiState {
    object Loading : LoginUiState()
    data class Success(val data: List<LoginResponse>) : LoginUiState()

    data class showError(val message: String, val type: ErrorType) : LoginUiState()
}

class LoginViewModel(
    private val loginUserUseCase: LoginUserUseCase
) : ViewModel() {
    private val _loginState = MutableLiveData<LoginUiState>()
    val loginState: LiveData<LoginUiState> = _loginState

    @SuppressLint("CheckResult")
    fun loginUser(email: String, password: String) {
        _loginState.value = LoginUiState.Loading
        val login = LoginRequest(
            email, password
        )
        loginUserUseCase.execute(login)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { apiresult ->
                    when (apiresult) {
                        is ApiResult.Error -> {
                            _loginState.value =
                                LoginUiState.showError(apiresult.message, apiresult.type)
                        }

                        is ApiResult.Success -> {
                            _loginState.value = LoginUiState.Success(listOf(apiresult.data))
                        }
                    }
                }
            )
    }
}
