package su.mandora.authlib.yggdrasil

import su.mandora.authlib.Environment

enum class YggdrasilEnvironment(authHost: String, accountsHost: String, sessionHost: String, servicesHost: String) {
    // Warning: these auth-servers are likely outdated

    PROD("https://authserver.mojang.com", "https://api.mojang.com", "https://sessionserver.mojang.com", "https://api.minecraftservices.com"),
    STAGING("https://yggdrasil-auth-staging.mojang.com", "https://api-staging.mojang.com", "https://yggdrasil-auth-session-staging.mojang.zone", "https://api-staging.minecraftservices.com");

    val environment = Environment.create(authHost, accountsHost, sessionHost, servicesHost, name)

    companion object {
        const val PROD_AUTH_HOST = "https://authserver.mojang.com" // Duplicate the auth host here so that you can access it at compile-time
    }
}