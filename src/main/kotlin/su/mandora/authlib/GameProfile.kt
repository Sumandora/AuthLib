package su.mandora.authlib

import kotlinx.serialization.Serializable

@Serializable
data class GameProfile(
    val id: String? = null,
    val name: String? = null,
    val legacy: Boolean = false
)
