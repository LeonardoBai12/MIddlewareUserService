package io.lb.middleware.user.util

import io.ktor.http.HttpStatusCode
import io.lb.middleware.user.data.model.UserData
import io.lb.middleware.user.domain.repository.UserRepository
import io.lb.middleware.util.MiddlewareException
import org.mindrot.jbcrypt.BCrypt

suspend fun UserRepository.validateEmail(email: String?) {
    if (email != null && isEmailAlreadyInUse(email)) {
        throw MiddlewareException(
            HttpStatusCode.Conflict,
            "Email already in use by another user."
        )
    }
}

suspend fun UserRepository.validatePasswordByEmail(
    email: String,
    password: String,
): UserData {
    val storedUser = getUserByEmail(email) ?: run {
        throw MiddlewareException(HttpStatusCode.NotFound, "There is no user with such email")
    }

    password.ifEmpty {
        throw MiddlewareException(HttpStatusCode.Unauthorized, "Invalid password")
    }

    if (!password.passwordCheck(storedUser.password ?: "")) {
        throw MiddlewareException(HttpStatusCode.Unauthorized, "Invalid password")
    }

    return storedUser
}

suspend fun UserRepository.validatePassword(
    userId: String,
    password: String,
): UserData {
    val storedUser = getUserById(userId) ?: run {
        throw MiddlewareException(HttpStatusCode.NotFound, "There is no user with such ID")
    }

    password.ifEmpty {
        throw MiddlewareException(HttpStatusCode.Unauthorized, "Invalid password")
    }

    if (!password.passwordCheck(storedUser.password ?: "")) {
        throw MiddlewareException(HttpStatusCode.Unauthorized, "Invalid password")
    }

    return storedUser
}


fun String.encrypt(): String? {
    val salt = BCrypt.gensalt(12)
    return BCrypt.hashpw(this, salt)
}

fun String.isStrongPassword(): Boolean {
    val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!?])(?=\\S+$).{8,}$"
    return this.matches(passwordRegex.toRegex())
}

fun String.passwordCheck(encryptedPassword: String): Boolean {
    return BCrypt.checkpw(this, encryptedPassword)
}

fun String?.isValidEmail(): Boolean {
    this ?: return false
    val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$")
    return isNotBlank() && matches(emailRegex)
}

fun String.isValidPhoneNumber(): Boolean {
    val phoneRegex = "^[0-9]{11}$"
    return this.matches(phoneRegex.toRegex())
}

