package mad.training.network.model.project

data class ProjectSummaryResponse(
    val id: String,
    val name: String,
    val shortDescription: String,
    val status: String,
    val lastUpdated: String
)
