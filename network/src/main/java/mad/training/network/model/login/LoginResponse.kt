package mad.training.network.model.login

data class LoginResponse(
    val user_id: String,
    val name: String,
    val surname: String,
    val lastname: String,
    val birthday: String,
    val gender: String,
    val telegram: String,
    val telegram_code: String,
    val email: String,
    val password: String
)