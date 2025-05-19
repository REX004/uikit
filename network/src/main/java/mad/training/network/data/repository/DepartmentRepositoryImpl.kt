package mad.training.network.data.repository

import io.reactivex.rxjava3.core.Single
import mad.training.network.data.remote.ApiService
import mad.training.network.domain.repository.DepartmentRepository
import mad.training.network.model.department.DepartmentResponse
import mad.training.network.util.ApiResult
import mad.training.network.util.NetworkErrorHandler


class DepartmentRepositoryImpl(
    private val api: ApiService
) : DepartmentRepository {

    override fun getDepartments(): Single<ApiResult<DepartmentResponse>> {
        return api.getDepartment()
            .map<ApiResult<DepartmentResponse>> { departmentResponse ->
                ApiResult.Success(departmentResponse)
            }
            .onErrorReturn { throwable ->
                NetworkErrorHandler.handleThrowable(throwable)
            }
    }
}