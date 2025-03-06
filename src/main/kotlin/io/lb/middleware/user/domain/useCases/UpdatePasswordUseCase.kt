package io.lb.middleware.user.domain.useCases

import io.ktor.http.HttpStatusCode
import io.lb.middleware.user.domain.repository.UserRepository
import io.lb.middleware.user.util.encrypt
import io.lb.middleware.user.util.validatePassword
import io.lb.middleware.util.MiddlewareException

/**
 * Use case for updating user password.
 *
 * @property repository The repository for interacting with user data.
 */
class UpdatePasswordUseCase(
    private val repository: UserRepository
) {
    /**
     * Updates the password for a user.
     *
     * @param userId The ID of the user whose password is being updated.
     * @param password The current password of the user.
     * @param newPassword The new password for the user.
     */
    suspend operator fun invoke(
        userId: String,
        password: String,
        newPassword: String
    ) {
        repository.validatePassword(userId, password)

        if (newPassword.length < 8) {
            throw MiddlewareException(
                HttpStatusCode.Conflict,
                "Password must have more than 8 characters."
            )
        }
        val hashedPassword = newPassword.encrypt()
        repository.updatePassword(userId, hashedPassword!!)
    }
}
