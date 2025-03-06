package io.lb.middleware.user.domain.useCases

import io.ktor.http.HttpStatusCode
import io.lb.middleware.model.TokenConfig
import io.lb.middleware.security.data.model.TokenClaim
import io.lb.middleware.security.generateToken
import io.lb.middleware.user.data.model.LoginResponse
import io.lb.middleware.user.domain.repository.UserRepository
import io.lb.middleware.util.MiddlewareException

/**
 * Use case for user login.
 *
 * @property repository The repository for interacting with user data.
 * @property tokenConfig The configuration for generating authentication tokens.
 */
class LoginByPhoneUseCase(
    private val repository: UserRepository,
    private val tokenConfig: TokenConfig
) {
    /**
     * Authenticates a user and generates an authentication token.
     *
     * @param phone The email of the user attempting to log in.
     * @return An authentication token.
     * @throws MiddlewareException if the provided email is invalid.
     * @throws MiddlewareException if the provided password is invalid.
     */
    suspend operator fun invoke(phone: String): LoginResponse {
        val user = repository.getUserByPhone(phone) ?: run {
            throw MiddlewareException(
                HttpStatusCode.NotFound,
                "There is no user with such phone number"
            )
        }

        return LoginResponse(
            userId = user.userId,
            token = generateToken(
                config = tokenConfig,
                TokenClaim(
                    name = "userId",
                    value = user.userId
                )
            )
        )
    }
}