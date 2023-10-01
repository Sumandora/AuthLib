package su.mandora.authlib.yggdrasil.response

data class ErrorResponse(
    val error: String? = null,
    val errorMessage: String? = null,
    val cause: String? = null
)