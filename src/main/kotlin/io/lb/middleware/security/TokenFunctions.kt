package io.lb.middleware.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.lb.middleware.security.data.model.TokenConfig
import io.lb.middleware.security.data.model.TokenClaim
import java.sql.Date

/**
 * Generates a JWT Bearer Token for the new user session.
 *
 * @param config Data class representing token configurations.
 * @param claims Data class representing a request to generate a tokem.
 */
fun generateToken(
    config: TokenConfig,
    vararg claims: TokenClaim
): String {
    var token = JWT.create()
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .withExpiresAt(Date(System.currentTimeMillis() + config.expiresIn))
    claims.forEach { claim ->
        token = token.withClaim(claim.name, claim.value)
    }
    return token.sign(Algorithm.HMAC256(config.secret))
}
