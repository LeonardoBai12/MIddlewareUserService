package io.lb.middleware.util

import io.ktor.http.HttpStatusCode

data class MiddlewareException(
    val code: HttpStatusCode,
    override val message: String?
) : Exception()
