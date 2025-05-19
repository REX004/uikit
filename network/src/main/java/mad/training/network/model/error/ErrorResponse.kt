package mad.training.network.model.error

data class ErrorResponse(
    val errorCode: String,
    val message: String,
    val details: List<String>
)