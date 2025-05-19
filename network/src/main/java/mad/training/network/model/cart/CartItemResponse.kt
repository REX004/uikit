package mad.training.network.model.cart

data class CartItemResponse(
    val itemId: String,
    val productId: String,
    val productName: String,
    val quantity: Int,
    val pricePerUnit: Double,
    val imageUrl: String,
    val totalPriceForItem: Double
)
