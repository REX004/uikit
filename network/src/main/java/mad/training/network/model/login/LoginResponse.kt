package mad.training.network.model.login

data class LoginResponse(
    val userId: String,
    val userName: String,
    val accessToken: String
)