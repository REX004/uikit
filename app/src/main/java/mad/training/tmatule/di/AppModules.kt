package mad.training.tmatule.di

import mad.training.network.di.networkModule
import mad.training.tmatule.presentation.auth.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { LoginViewModel() }
}

val domainModule = module {

}
val appModules = listOf(
    presentationModule,
    domainModule,
    networkModule
)