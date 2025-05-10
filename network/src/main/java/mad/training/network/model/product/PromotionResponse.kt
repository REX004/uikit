package mad.training.network.model.product

data class PromotionResponse(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val startDate: String,
    val endDate: String
)
