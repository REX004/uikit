package mad.training.network.data.repository

import io.reactivex.rxjava3.core.Single
import mad.training.network.data.remote.ApiService
import mad.training.network.domain.repository.ContentRepository
import mad.training.network.domain.repository.Result
import mad.training.network.model.error.NetworkError
import mad.training.network.model.product.ProductDetailResponse
import mad.training.network.model.product.PromotionResponse

class ContentRepositoryImpl(
    private val api: ApiService
) : ContentRepository {
    override fun getPromotions(): Single<Result<List<PromotionResponse>, NetworkError>> {
        return wrapNetworkRequest(api.getPromotions())
    }

    override fun getCatalog(categoryId: String?): Single<Result<List<ProductDetailResponse>, NetworkError>> {
        return wrapNetworkRequest(api.getCatalog(categoryId!!))
    }

    override fun searchProducts(query: String): Single<Result<List<ProductDetailResponse>, NetworkError>> {
        return wrapNetworkRequest(api.searchProducts(query))
    }

    override fun getProductDescription(productId: String): Single<Result<ProductDetailResponse, NetworkError>> {
        return wrapNetworkRequest(api.getProductDescription(productId))
    }
}