package mad.training.tmatule.di

import mad.training.network.di.networkModule
import mad.training.tmatule.domain.usecase.auth.LoginUserUseCase
import mad.training.tmatule.domain.usecase.department.GetDepartmentsUseCase
import mad.training.tmatule.presentation.auth.LoginViewModel
import mad.training.tmatule.presentation.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get()) }
}

val domainModule = module {
    factory { GetDepartmentsUseCase(get()) }
    factory { LoginUserUseCase(get()) }
}
val appModules = listOf(
    presentationModule,
    domainModule,
    networkModule
)