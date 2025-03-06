package io.lb.middleware.user.domain.useCases

import io.ktor.http.HttpStatusCode
import io.lb.middleware.user.data.model.UserUpdateRequest
import io.lb.middleware.user.domain.repository.UserRepository
import io.lb.middleware.user.util.isValidEmail
import io.lb.middleware.user.util.validateEmail
import io.lb.middleware.user.util.validatePassword
import io.lb.middleware.util.MiddlewareException

/**
 * Use case for updating user information.
 *
 * @property repository The repository for interacting with user data.
 */
class UpdateUserUseCase(
    private val repository: UserRepository
) {
    /**
     * Updates the information of a user.
     *
     * @param userId The ID of the user whose information is being updated.
     * @param user The update request containing the new user information.
     */
    suspend operator fun invoke(userId: String, user: UserUpdateRequest) {
        repository.validateEmail(user.email)
        val storedUser = repository.validatePassword(userId, user.password)

        if (user.userName != null && user.userName.isBlank()) {
            throw MiddlewareException(HttpStatusCode.Conflict, "User must have a name.")
        }

        if (user.email.isValidEmail().not()) {
            throw MiddlewareException(HttpStatusCode.Conflict, "Invalid email.")
        }

        val updatedUser = storedUser.copy(
            userName = user.userName ?: storedUser.userName,
            email = user.email ?: storedUser.email,
            profilePictureUrl = user.profilePictureUrl ?: storedUser.profilePictureUrl,
        )
        repository.updateUser(updatedUser)
    }
}
