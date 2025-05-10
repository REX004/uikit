package mad.training.network.domain.repository

import io.reactivex.rxjava3.core.Single
import mad.training.network.model.error.NetworkError
import mad.training.network.model.product.ProductDetailResponse
import mad.training.network.model.product.PromotionResponse

interface ContentRepository {
    fun getPromotions(): Single<Result<List<PromotionResponse>, NetworkError>>
    fun getCatalog(categoryId: String?): Single<Result<List<ProductDetailResponse>, NetworkError>>
    fun searchProducts(query: String): Single<Result<List<ProductDetailResponse>, NetworkError>>
    fun getProductDescription(productId: String): Single<Result<ProductDetailResponse, NetworkError>>
}