package io.lb.middleware.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import io.lb.middleware.session.MiddlewareSession

private const val SESSION_NAME = "MiddlewareSessions"

fun Application.configureSession() {
    install(Sessions) {
        cookie<MiddlewareSession>(SESSION_NAME)
    }
}
