package io.lb.middleware.user.data.model

import kotlinx.serialization.Serializable

/**
 * Data class representing a request to delete a user.
 *
 * @property password The user's password for authentication.
 */
@Serializable
data class ProtectedUserRequest(
    val password: String,
)
