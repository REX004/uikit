package mad.training.network.model.profile

data class ProfileInfoResponse(
    val userId: String,
    val name: String,
    val email: String,
    val password: String,
    val avatarUrl: String,
    val registrationDate: String
)
