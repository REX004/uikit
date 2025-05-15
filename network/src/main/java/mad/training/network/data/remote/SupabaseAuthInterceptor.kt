package mad.training.network.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class SupabaseAuthInterceptor(
    private val tokenProvider: TokenProvider
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

//        requestBuilder.header("apiKey", tokenProvider.getSupabaseApiKey())

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}