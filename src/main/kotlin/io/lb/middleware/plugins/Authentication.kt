package io.lb.middleware.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.lb.middleware.core.embedded
import io.lb.middleware.security.data.model.TokenConfig
import io.lb.middleware.user.domain.useCases.UserUseCases

fun Application.configureAuth(useCases: UserUseCases) {
    val config = TokenConfig.buildTokenConfig(embedded)
    authentication {
        jwt {
            realm = "Middleware"
            verifier(
                JWT.require(Algorithm.HMAC256(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()
            )
            validate { credential ->
                val userId = credential.payload.getClaim("userId").asString()
                val email = credential.payload.getClaim("email").asString()

                if (credential.payload.audience.contains(config.audience) && userExists(useCases, userId, email)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}

suspend fun userExists(useCases: UserUseCases, userId: String, email: String): Boolean {
    val user = runCatching {
        useCases.getUserByIdUseCase.invoke(userId)
    }.getOrNull()
    return user?.email == email
}
