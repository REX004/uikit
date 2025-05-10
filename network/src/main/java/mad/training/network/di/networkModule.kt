package mad.training.network.di

import mad.training.network.data.remote.ApiService
import mad.training.network.data.remote.ConnectivityInterceptor
import mad.training.network.data.remote.HttpClient
import mad.training.network.data.remote.SharedPreferencesTokenProvider
import mad.training.network.data.remote.SupabaseAuthInterceptor
import mad.training.network.data.remote.TokenProvider
import mad.training.network.data.repository.AuthRepositoryImpl
import mad.training.network.data.repository.ContentRepositoryImpl
import mad.training.network.domain.repository.AuthRepository
import mad.training.network.domain.repository.CartRepository
import mad.training.network.domain.repository.ContentRepository
import mad.training.network.domain.repository.ProfileRepository
import mad.training.network.domain.repository.ProjectsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module {

    single<TokenProvider> {
        SharedPreferencesTokenProvider()

    }

    single { SupabaseAuthInterceptor(get()) }

    single { ConnectivityInterceptor(androidContext()) }

    single {
        HttpClient.createOkHttpClient(
            connectivityInterceptor = get(),
            supabaseAuthInterceptor = get(),
        )
    }

    single {
        HttpClient.createApiService<ApiService>(get())
    }

    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<ContentRepository> { ContentRepositoryImpl(get()) }
//    single<CartRepository> { CartRepositoryImpl(get()) }
//    single<ProjectsRepository> { ProjectsRepositoryImpl(get()) }
//    single<ProfileRepository> { ProfileRepositoryImpl(get()) }
}