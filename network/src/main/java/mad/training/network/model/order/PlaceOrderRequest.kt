package mad.training.network.model.order

data class PlaceOrderRequest(
    val deliveryAddressId: String,
    val paymentMethod: String,
    val customerNotes: String? = null
)