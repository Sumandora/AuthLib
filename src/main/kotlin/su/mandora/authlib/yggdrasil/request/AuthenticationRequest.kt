package su.mandora.authlib.yggdrasil.request

import kotlinx.serialization.Serializable
import su.mandora.authlib.Agent

@Serializable
data class AuthenticationRequest(
    val agent: Agent,
    val username: String,
    val password: String,
    val clientToken: String? = null) {
    @Suppress("unused") // Required for serialization
    private val requestUser = true
}
