package io.lb.middleware.user.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RequestSmsData(
    val phone: String,
)
