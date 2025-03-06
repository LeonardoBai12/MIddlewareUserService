package io.lb.middleware.session

import kotlinx.serialization.Serializable

@Serializable
data class MiddlewareSession(
    val clientId: String,
    val sessionId: String,
)
