package mad.training.network.model.product

data class ProductDetailResponse(
    val id: String,
    val name: String,
    val fullDescription: String,
    val price: Double,
    val currency: String,
    val images: List<String>,
    val specifications: List<String>,
    val stockOut: Int
)