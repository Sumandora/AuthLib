package su.mandora.authlib

enum class UserType(@get:JvmName("getName") val stylizedName: String) {
    LEGACY("legacy"),
    MOJANG("mojang")
}