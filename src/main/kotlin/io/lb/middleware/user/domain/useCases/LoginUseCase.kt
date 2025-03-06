package io.lb.middleware.user.domain.useCases

import io.lb.middleware.security.data.model.TokenConfig
import io.lb.middleware.security.data.model.TokenClaim
import io.lb.middleware.security.generateToken
import io.lb.middleware.user.data.model.LoginResponse
import io.lb.middleware.user.domain.repository.UserRepository
import io.lb.middleware.user.util.validatePasswordByEmail
import io.lb.middleware.util.MiddlewareException

/**
 * Use case for user login.
 *
 * @property repository The repository for interacting with user data.
 * @property tokenConfig The configuration for generating authentication tokens.
 */
class LoginUseCase(
    private val repository: UserRepository,
    private val tokenConfig: TokenConfig
) {
    /**
     * Authenticates a user and generates an authentication token.
     *
     * @param email The email of the user attempting to log in.
     * @param password The password provided by the user.
     * @return An authentication token.
     * @throws MiddlewareException if the provided email is invalid.
     * @throws MiddlewareException if the provided password is invalid.
     */
    suspend operator fun invoke(email: String, password: String): LoginResponse {
        val user = repository.validatePasswordByEmail(email, password)

        return LoginResponse(
            userId = user.userId,
            token = generateToken(
                config = tokenConfig,
                TokenClaim(
                    name = USER_ID_CLAIM,
                    value = user.userId
                ),
                TokenClaim(
                    name = EMAIL_CLAIM,
                    value = user.email
                )
            )
        )
    }

    companion object {
        private const val EMAIL_CLAIM = "email"
        private const val USER_ID_CLAIM = "userId"
    }
}
