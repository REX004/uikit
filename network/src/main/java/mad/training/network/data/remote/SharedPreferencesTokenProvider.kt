package mad.training.network.data.remote

class SharedPreferencesTokenProvider(

) : TokenProvider {

    private val supabaseAnonKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNqYWNodnZxZXpicHVpZnlpbGdxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDY4OTE2ODksImV4cCI6MjA2MjQ2NzY4OX0.3rTQYgrdxTjTi9KvNVtnWkBgZXP0IVUc7AouzNDPqDc"
    override fun getSupabaseApiKey(): String {
        return supabaseAnonKey
    }

    override fun getAuthToken(): String? {
        return null
    }
}