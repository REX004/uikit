package mad.training.network.model.cart

data class CartResponse(
    val cartId: String,
    val items: List<CartItemResponse>,
    val totalItems: Int,
    val subtotalItems: Double,
    val totalPrice: Double,
    val currency: String
)