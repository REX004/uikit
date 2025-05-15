package mad.training.tmatule.presentation.home

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import mad.training.network.model.department.Department
import mad.training.network.util.ApiResult
import mad.training.network.util.ErrorType
import mad.training.tmatule.domain.usecase.department.GetDepartmentsUseCase


sealed class DepartmentsUiState {
    object Loading : DepartmentsUiState()
    data class Success(val data: List<Department>) : DepartmentsUiState()

    data class ShowError(val message: String, val type: ErrorType) : DepartmentsUiState()
}

class HomeViewModel(
    private val getDepartmentsUseCase: GetDepartmentsUseCase
) : ViewModel() {
    private val _uiState = MutableLiveData<DepartmentsUiState>()
    val uiState: LiveData<DepartmentsUiState> = _uiState

    @SuppressLint("CheckResult")
    fun fetchDepartments() {
        _uiState.value = DepartmentsUiState.Loading
        getDepartmentsUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { apiResult -> // ApiResult<List<Department>>
                    when (apiResult) {
                        is ApiResult.Success -> {
                            _uiState.value = DepartmentsUiState.Success(apiResult.data)
                        }

                        is ApiResult.Error -> {
                            _uiState.value =
                                DepartmentsUiState.ShowError(apiResult.message, apiResult.type)
                        }
                    }
                },
                { throwable -> // Совсем непредвиденные ошибки (редко)
                    _uiState.value = DepartmentsUiState.ShowError(
                        throwable.message ?: "Неизвестная ошибка в потоке",
                        ErrorType.GENERAL_SERVER_ERROR // Считаем общей
                    )
                }
            )
    }
}