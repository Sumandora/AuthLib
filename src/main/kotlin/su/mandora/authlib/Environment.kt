package su.mandora.authlib

// Only auth is important all the other variables are meaningless
data class Environment(val auth: String, val account: String? = null, val session: String? = null, val services: String? = null, val name: String? = null) {
    companion object {
        @JvmStatic
        fun create(auth: String, account: String? = null, session: String? = null, services: String? = null, name: String? = null): Environment {
            return Environment(auth, account, session, services, name)
        }
    }
}