package mad.training.network.data.remote

interface TokenProvider {
    fun getSupabaseApiKey(): String
    fun getAuthToken(): String?
}