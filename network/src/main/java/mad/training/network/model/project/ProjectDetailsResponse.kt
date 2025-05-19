package mad.training.network.model.project

data class ProjectDetailsResponse(
    val id: String,
    val name: String,
    val fullDescription: String,
    val status: String,
    val owner: String,
    val creationDate: String,
    val lastUpdatedDate: String
)
