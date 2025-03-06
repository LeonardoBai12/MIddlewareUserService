package io.lb.middleware.user.domain.useCases

import io.ktor.http.HttpStatusCode
import io.lb.middleware.user.data.model.UserCreateRequest
import io.lb.middleware.user.data.model.UserData
import io.lb.middleware.user.domain.repository.UserRepository
import io.lb.middleware.user.util.encrypt
import io.lb.middleware.user.util.isValidEmail
import io.lb.middleware.user.util.isValidPhoneNumber
import io.lb.middleware.user.util.validateEmail
import io.lb.middleware.util.MiddlewareException

/**
 * Use case for user sign up.
 *
 * @property repository The repository for interacting with user data.
 */
class SignUpUseCase(
    private val repository: UserRepository
) {
    /**
     * Registers a new user.
     *
     * @param user The user creation request containing user details.
     * @return The ID of the newly created user.
     * @throws MiddlewareException if there are validation errors or if user creation fails.
     */
    suspend operator fun invoke(user: UserCreateRequest): String {
        repository.validateEmail(user.email)

        if (user.userName.isBlank()) {
            throw MiddlewareException(HttpStatusCode.Conflict, "User must have a name.")
        }

        if (user.email.isValidEmail().not()) {
            throw MiddlewareException(HttpStatusCode.Conflict, "Invalid email.")
        }

        if (user.phone.isValidPhoneNumber().not()) {
            throw MiddlewareException(HttpStatusCode.Conflict, "Invalid phone number.")
        }

        if (user.password.length < 8) {
            throw MiddlewareException(
                HttpStatusCode.Conflict,
                "Password must have more than 8 characters."
            )
        }

        val hashedPassword = user.password.encrypt()
        val userData = UserData(
            userName = user.userName,
            phone = user.phone,
            password = hashedPassword,
            email = user.email,
            profilePictureUrl = user.profilePictureUrl,
        )
        repository.createUser(userData)
        return userData.userId
    }
}
