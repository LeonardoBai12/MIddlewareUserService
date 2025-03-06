package io.lb.middleware.core

import io.ktor.server.application.Application
import io.lb.middleware.user.domain.useCases.UserUseCases
import io.lb.middleware.user.routes.userRoutes

const val embedded: Boolean = false

fun Application.routes(useCases: UserUseCases) {
    userRoutes(
        useCases = useCases
    )
}
