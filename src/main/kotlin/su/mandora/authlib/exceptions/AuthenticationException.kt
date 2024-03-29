package su.mandora.authlib.exceptions

open class AuthenticationException : Exception {
    constructor()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
