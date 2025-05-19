package mad.training.network.model.order

data class OrderConfirmationResponse(
    val orderId: String,
    val orderStatus: String,
    val estimatedDeliveryDate: String,
    val totalAmount: Double,
    val currency: String,
    val message: String,
)
