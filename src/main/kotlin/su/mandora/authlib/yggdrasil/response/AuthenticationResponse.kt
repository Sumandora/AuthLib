package su.mandora.authlib.yggdrasil.response

import kotlinx.serialization.Serializable
import su.mandora.authlib.GameProfile

@Serializable
data class AuthenticationResponse(
    val accessToken: String? = null,
    val clientToken: String? = null,
    val selectedProfile: GameProfile? = null,
    val availableProfiles: List<GameProfile>
)