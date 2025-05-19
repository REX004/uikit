package mad.training.network.domain.repository

import io.reactivex.rxjava3.core.Single
import mad.training.network.model.department.DepartmentResponse
import mad.training.network.util.ApiResult
import retrofit2.Response

interface DepartmentRepository {
    fun getDepartments(): Single<ApiResult<DepartmentResponse>>
}