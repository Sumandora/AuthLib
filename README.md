# AuthLib
Drop-in replacement for yggdrasil minecraft/scrolls logins

## Usage

```kotlin
val yggdrasilAuthenticationService = YggdrasilAuthenticationService(Proxy.NO_PROXY, YggdrasilEnvironment.PROD.environment)
val userAuthentication = YggdrasilUserAuthentication(yggdrasilAuthenticationService, Agent.MINECRAFT, YggdrasilEnvironment.PROD.environment)
userAuthentication.setUsername("...")
userAuthentication.setPassword("...")
userAuthentication.logIn()
if (userAuthentication.isLoggedIn) {
    println("Logged in: ${userAuthentication.selectedProfile}")
}
```

```java
YggdrasilAuthenticationService yggdrasilAuthenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, YggdrasilEnvironment.PROD.getEnvironment());
YggdrasilUserAuthentication userAuthentication = new YggdrasilUserAuthentication(yggdrasilAuthenticationService, Agent.MINECRAFT, YggdrasilEnvironment.PROD.getEnvironment());
userAuthentication.setUsername("...");
userAuthentication.setPassword("...");
userAuthentication.logIn();
if (userAuthentication.isLoggedIn()) {
    System.out.println("Logged in: " + userAuthentication.getSelectedProfile());
}
```

## Features
- Custom Environments
- Identical interface to Mojang AuthLib
- Kotlin-syntax support