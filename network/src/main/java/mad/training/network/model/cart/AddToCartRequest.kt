package mad.training.network.model.cart

data class AddToCartRequest(
    val productId: String,
    val quantity: Int
)