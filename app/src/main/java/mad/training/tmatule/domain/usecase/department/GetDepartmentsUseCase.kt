package mad.training.tmatule.domain.usecase.department

import io.reactivex.rxjava3.core.Single
import mad.training.network.domain.repository.DepartmentRepository
import mad.training.network.model.department.Department
import mad.training.network.util.ApiResult

class GetDepartmentsUseCase(
    private val departmentsRepository: DepartmentRepository
) {
    fun execute(): Single<ApiResult<List<Department>>> {
        return departmentsRepository.getDepartments()
            .map { result ->
                when (result) {
                    is ApiResult.Success -> ApiResult.Success(result.data.data)
                    is ApiResult.Error -> ApiResult.Error(
                        result.message,
                        result.type
                    )
                }
            }
    }
}