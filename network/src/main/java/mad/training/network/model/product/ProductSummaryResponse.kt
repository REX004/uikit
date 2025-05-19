package mad.training.network.model.product

data class ProductSummaryResponse(
    val id: String,
    val name: String,
    val price: Double,
    val currency: String,
    val imageUrl: String,
    val shortDescription: String,
    val rating: Int
)