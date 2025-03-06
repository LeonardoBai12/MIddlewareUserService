package io.lb.middleware.user.domain.useCases

import io.ktor.http.HttpStatusCode
import io.lb.middleware.user.data.model.UserData
import io.lb.middleware.user.domain.repository.UserRepository
import io.lb.middleware.util.MiddlewareException

/**
 * Use case for retrieving a user by their ID.
 *
 * @property repository The repository for interacting with user data.
 */
class GetUserByIdUseCase(
    private val repository: UserRepository
) {
    /**
     * Retrieves a user by their ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The user data if found.
     * @throws MiddlewareException if no user is found with the specified ID.
     */
    suspend operator fun invoke(userId: String): UserData {
        return repository.getUserById(userId) ?: throw MiddlewareException(
            HttpStatusCode.NotFound,
            "There is no user with such ID"
        )
    }
}
