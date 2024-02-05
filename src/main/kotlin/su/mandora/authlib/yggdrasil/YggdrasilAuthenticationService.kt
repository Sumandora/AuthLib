package su.mandora.authlib.yggdrasil

import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import su.mandora.authlib.Environment
import su.mandora.authlib.exceptions.*
import su.mandora.authlib.yggdrasil.response.ErrorResponse
import java.io.IOException
import java.net.HttpURLConnection
import java.net.Proxy
import java.net.URL
import java.nio.charset.StandardCharsets

class YggdrasilAuthenticationService(private val proxy: Proxy, val clientToken: String?, environment: Environment) {

    companion object {
        val serializer = Json {
            ignoreUnknownKeys = true // I have not included the full responses since most keys are not needed for logging in
        }
    }


    constructor(proxy: Proxy) : this(proxy, null, YggdrasilEnvironment.PROD.environment)

    constructor(proxy: Proxy, environment: Environment) : this(proxy, null, environment)

    constructor(proxy: Proxy, clientToken: String?) : this(proxy, clientToken, YggdrasilEnvironment.PROD.environment)

    // Only post requests are supported as they are the only thing we need for logging in
    inline fun <reified T, reified R> makeRequest(url: URL, input: R, @Suppress("UNUSED_PARAMETER") /*Used for type inference*/ response: Class<T>? = null): T {
        return try {
            val jsonResult: String = performPostRequest(url, serializer.encodeToString(input), "application/json")
            val result = try {
                serializer.decodeFromString<T>(jsonResult)
            } catch (e: SerializationException) {
                val result = try {
                    serializer.decodeFromString<ErrorResponse>(jsonResult)
                } catch (e2: SerializationException) {
                    throw SerializationException("Can't deserialize response: $jsonResult", e)
                }
                if (result.error?.isNotBlank() == true && result.errorMessage != null) {
                    if ("UserMigratedException" == result.cause) {
                        throw UserMigratedException(result.errorMessage)
                    }
                    if ("ForbiddenOperationException" == result.error) {
                        throw InvalidCredentialsException(result.errorMessage)
                    }
                    if ("InsufficientPrivilegesException" == result.error) {
                        throw InsufficientPrivilegesException(result.errorMessage)
                    }
                    if ("multiplayer.access.banned" == result.error) {
                        throw UserBannedException()
                    }
                    throw AuthenticationException(result.errorMessage)
                }
                error("Unknown error response: $result")
            }
            result
        } catch (e: IOException) {
            throw AuthenticationUnavailableException("Cannot contact authentication server", e)
        } catch (e: IllegalStateException) {
            throw AuthenticationUnavailableException("Cannot contact authentication server", e)
        }
    }

    fun performPostRequest(url: URL, post: String, contentType: String): String {
        val connection = url.openConnection(proxy) as HttpURLConnection
        connection.setConnectTimeout(15000)
        connection.setReadTimeout(15000)
        connection.setUseCaches(false)
        val postAsBytes = post.toByteArray(StandardCharsets.UTF_8)
        connection.setRequestProperty("Content-Type", "$contentType; charset=utf-8")
        connection.setRequestProperty("Content-Length", postAsBytes.size.toString())
        connection.setDoOutput(true)

        connection.outputStream.write(postAsBytes)

        return connection.inputStream.readBytes().decodeToString()
    }

}
