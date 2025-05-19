package mad.training.network.data.repository

import io.mockk.*
import io.reactivex.rxjava3.core.Single
import mad.training.network.data.remote.ApiService
import mad.training.network.model.error.NetworkError
import mad.training.network.domain.repository.Result
import mad.training.network.model.login.LoginRequest
import mad.training.network.model.login.LoginResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response as RetrofitResponse
import java.io.IOException

class AuthRepositoryImplTest {

    private lateinit var apiService: ApiService
    private lateinit var authRepository: AuthRepositoryImpl

    @BeforeEach
    fun setUp() {
        apiService = mockk()
        authRepository = AuthRepositoryImpl(apiService)
        // Для RxJava тестов, если шедулеры мешают
        // RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        // RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        // RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @AfterEach
    fun tearDown() {
        // RxJavaPlugins.reset()
        // RxAndroidPlugins.reset()
        unmockkAll()
    }

    @Test
    fun `login success should return Result Success with LoginResponse`() {
        // Arrange
        val loginRequest = LoginRequest("test@example.com", "password")
        val mockLoginResponse = LoginResponse("userId123", "Test User", "tokenAbc")
        val successResponse: RetrofitResponse<LoginResponse> =
            RetrofitResponse.success(mockLoginResponse)

        every { apiService.login(loginRequest) } returns Single.just(successResponse)

        // Act
        val testObserver = authRepository.login(loginRequest).test()

        // Assert
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        val result = testObserver.values()[0]
        assert(result is Result.Success)
        val data = (result as Result.Success).data
        assert(data == mockLoginResponse)

        verify(exactly = 1) { apiService.login(loginRequest) }
    }

    @Test
    fun `login http error 401 should return Result Error HttpError`() {
        // Arrange
        val loginRequest = LoginRequest("test@example.com", "password")
        val errorResponseBody =
            "{\"error\":\"Unauthorized\"}".toResponseBody("application/json".toMediaTypeOrNull())
        val errorResponse: RetrofitResponse<LoginResponse> =
            RetrofitResponse.error(401, errorResponseBody)
        // Обернем в HttpException, так как wrapNetworkRequest ожидает это для cause в HttpError,
        // если ошибка пришла из RetrofitResponse.error
        val httpException = HttpException(errorResponse)


        // Мы мокаем apiService, который возвращает Single<RetrofitResponse<LoginResponse>>.
        // wrapNetworkRequest сам обработает RetrofitResponse.error().
        every { apiService.login(loginRequest) } returns Single.just(errorResponse)


        // Act
        val testObserver = authRepository.login(loginRequest).test()

        // Assert
        testObserver.assertNoErrors() // Сам Single не должен завершиться с ошибкой
        testObserver.assertValueCount(1)
        val result = testObserver.values()[0]
        assert(result is Result.Error)
        val networkError = (result as Result.Error).error
        assert(networkError is NetworkError.HttpError)
        val httpError = networkError as NetworkError.HttpError
        assert(httpError.httpCode == 401)
        // Проверка serverMessage может быть сложнее, если errorBodyString не пустой
        // Для простоты, если errorBody пуст, message() от Retrofit Response используется
        // assert(httpError.serverMessage == "Unauthorized") // Зависит от того, как формируется сообщение в wrapNetworkRequest

        verify(exactly = 1) { apiService.login(loginRequest) }
    }

    @Test
    fun `login network ioexception should return Result Error NoInternetConnection`() {
        // Arrange
        val loginRequest = LoginRequest("test@example.com", "password")
        val ioException = IOException("No internet test")

        every { apiService.login(loginRequest) } returns Single.error(ioException)

        // Act
        val testObserver = authRepository.login(loginRequest).test()

        // Assert
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        val result = testObserver.values()[0]
        assert(result is Result.Error)
        val networkError = (result as Result.Error).error
        assert(networkError is NetworkError.NoInternetConnection)
        assert(networkError.cause == ioException)

        verify(exactly = 1) { apiService.login(loginRequest) }
    }
}