package mad.training.network.data.remote

import io.reactivex.rxjava3.core.Single
import mad.training.network.model.cart.AddToCartRequest
import mad.training.network.model.cart.CartResponse
import mad.training.network.model.cart.UpdateCartItemRequest
import mad.training.network.model.department.DepartmentResponse
import mad.training.network.model.login.LoginRequest
import mad.training.network.model.login.LoginResponse
import mad.training.network.model.order.OrderConfirmationResponse
import mad.training.network.model.order.PlaceOrderRequest
import mad.training.network.model.product.ProductDetailResponse
import mad.training.network.model.product.PromotionResponse
import mad.training.network.model.profile.CreateProfile
import mad.training.network.model.profile.OtpResponse
import mad.training.network.model.profile.ProfileInfoResponse
import mad.training.network.model.project.CreateObjectRequest
import mad.training.network.model.project.ProjectDetailsResponse
import mad.training.network.model.project.ProjectSummaryResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    companion object {
        const val BASE_URL = "https://cjachvvqezbpuifyilgq.supabase.co"
    }

    @GET("rest/v1/user_data")
    fun login(
        @Query("email") email: String,
        @Query("password") pass: String,
        @Query("limit") limit: Int = 1,
        @Query("select") select: String = "*",

        ):
            Single<List<LoginResponse>>

    @POST("profile_create_with_otp")
    fun createProfileAndOtp(
        @Body body: CreateProfile
    ): Single<Response<OtpResponse>>

    @GET("promotions")
    fun getPromotions():
            Single<Response<List<PromotionResponse>>>

    @GET("department")
    fun getDepartment(

    ): Single<DepartmentResponse>

    @GET("catalog")
    fun getCatalog(
        @Query("category_id") categoryId: String
    ): Single<Response<List<ProductDetailResponse>>>

    @GET("search")
    fun searchProducts(
        @Query("q") query: String
    ): Single<Response<List<ProductDetailResponse>>>

    @GET("products/{id}")
    fun getProductDescription(
        @Path("id") productId: String
    ): Single<Response<ProductDetailResponse>>

    @POST("cart/add")
    fun addToCart(
        @Body body: AddToCartRequest
    ): Single<Response<CartResponse>>

    @PUT("cart/item/{itemId}")
    fun updateCartItem(
        @Path("itemId") itemId: String,
        @Body body: UpdateCartItemRequest
    ): Single<Response<CartResponse>>


    @POST("orders/place")
    fun placeOrder(
        @Body body: PlaceOrderRequest
    ): Single<Response<OrderConfirmationResponse>>

    @GET("projects")
    fun getProjectsList():
            Single<Response<List<ProjectSummaryResponse>>>

    @POST("projects/create")
    fun createProject(@Body body: CreateObjectRequest): Single<Response<ProjectDetailsResponse>>

    @GET("projects/{id}")
    fun getProjectDetails(@Path("id") projectId: String): Single<Response<ProjectDetailsResponse>>

    @GET("profile/me")
    fun getProfileInfo(): Single<Response<ProfileInfoResponse>>

    @POST("auth/logout")
    fun logout(): Single<Response<Unit>>
}