package io.lb.middleware.user.domain.useCases

import io.lb.middleware.user.domain.repository.UserRepository
import io.lb.middleware.user.util.validatePassword
import io.lb.middleware.util.MiddlewareException

/**
 * Use case for deleting a user.
 *
 * @property repository The repository for interacting with user data.
 */
class DeleteUserUseCase(
    private val repository: UserRepository
) {
    /**
     * Deletes a user after validating the provided password.
     *
     * @param userId The ID of the user to delete.
     * @param password The password of the user to validate the deletion.
     * @throws MiddlewareException if the provided password is invalid.
     */
    suspend operator fun invoke(userId: String, password: String) {
        repository.validatePassword(userId, password)
        repository.deleteUser(userId)
    }
}
