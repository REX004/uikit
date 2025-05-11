package mad.training.tmatule.di

import mad.training.network.di.networkModule
import org.koin.dsl.module

val presentationModule = module {

}

val domainModule = module {

}
val appModules = listOf(
    presentationModule,
    domainModule,
    networkModule
)