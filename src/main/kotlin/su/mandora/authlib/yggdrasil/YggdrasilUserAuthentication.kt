package su.mandora.authlib.yggdrasil

import su.mandora.authlib.Agent
import su.mandora.authlib.Environment
import su.mandora.authlib.GameProfile
import su.mandora.authlib.UserType
import su.mandora.authlib.exceptions.AuthenticationException
import su.mandora.authlib.exceptions.InvalidCredentialsException
import su.mandora.authlib.yggdrasil.request.AuthenticationRequest
import su.mandora.authlib.yggdrasil.response.AuthenticationResponse
import java.net.URL
import java.util.*
import kotlin.jvm.optionals.getOrNull

class YggdrasilUserAuthentication(private val authenticationService: YggdrasilAuthenticationService, private val clientToken: String?, private val agent: Agent, private val routeAuthenticate: URL) {

    private var accessToken = Optional.empty<String>()

    constructor(authenticationService: YggdrasilAuthenticationService, agent: Agent) : this(authenticationService, authenticationService.clientToken, agent, YggdrasilEnvironment.PROD.environment)
    constructor(authenticationService: YggdrasilAuthenticationService, agent: Agent, env: Environment) : this(authenticationService, authenticationService.clientToken, agent, URL(env.auth + "/authenticate"))
    constructor(authenticationService: YggdrasilAuthenticationService, clientToken: String?, agent: Agent) : this(authenticationService, clientToken, agent, YggdrasilEnvironment.PROD.environment)

    constructor(authenticationService: YggdrasilAuthenticationService, clientToken: String?, agent: Agent, env: Environment) : this(authenticationService, clientToken, agent, URL(env.auth + "/authenticate"))


    // Java support
    private var username = Optional.empty<String>()
    fun setUsername(username: String) {
        this.username = Optional.of(username)
    }
    fun getUsername() = this.username.getOrNull()

    private var password = Optional.empty<String>()
    fun setPassword(password: String) {
        this.password = Optional.of(password)
    }
    fun getPassword() = this.password.getOrNull()

    private var userType = Optional.empty<UserType>()
    fun getUserType() = this.userType.getOrNull()


    val authenticatedToken: String?
        get() {
            return this.accessToken.getOrNull()
        }

    val isLoggedIn: Boolean
        get() {
            return accessToken.getOrNull()?.isNotBlank() == true
        }

    var selectedProfile: GameProfile? = null
    var profiles = emptyList<GameProfile>()

    fun logIn() {
        if(username.getOrNull()?.isBlank() != false)
            throw InvalidCredentialsException("Invalid username")
        if(password.getOrNull()?.isBlank() != false)
            throw InvalidCredentialsException("Invalid password")

        logInWithPassword()
    }

    private fun logInWithPassword() {
        val request = AuthenticationRequest(agent, username.get(), password.get(), clientToken)
        val response = authenticationService.makeRequest(this.routeAuthenticate, request, AuthenticationResponse::class.java)
        if (response.clientToken != this.clientToken) {
            throw AuthenticationException("Server requested we change our client token. Don't know how to handle this!")
        }

        selectedProfile = response.selectedProfile
        profiles = response.availableProfiles

        val profile = response.selectedProfile ?: response.availableProfiles.firstOrNull() ?: throw AuthenticationException("No profiles found") // Custom exception

        userType = Optional.of(if(profile.legacy) UserType.LEGACY else UserType.MOJANG)
        this.accessToken = Optional.of(response.accessToken ?: throw AuthenticationException("Access token is missing") /* Custom exception */)
    }

    fun logOut() {
        this.accessToken = Optional.empty()
    }

    fun canPlayOnline() = isLoggedIn
}