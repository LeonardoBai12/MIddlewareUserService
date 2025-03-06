package io.lb.middleware.user.domain.useCases

import io.lb.middleware.user.domain.repository.UserRepository
import io.lb.middleware.util.MiddlewareException

/**
 * Use case for retrieving a user by their ID.
 *
 * @property repository The repository for interacting with user data.
 */
class GetUserByPhoneUseCase(
    private val repository: UserRepository
) {
    /**
     * Retrieves a user by their ID.
     *
     * @param phone The ID of the user to retrieve.
     * @return The user data if found.
     * @throws MiddlewareException if no user is found with the specified ID.
     */
    suspend operator fun invoke(phone: String): Boolean {
        return repository.isPhoneAlreadyInUse(phone)
    }
}
