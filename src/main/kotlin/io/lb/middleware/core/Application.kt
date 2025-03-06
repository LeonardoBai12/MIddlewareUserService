package io.lb.middleware.core

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.lb.middleware.plugins.configureAuth
import io.lb.middleware.plugins.configureMonitoring
import io.lb.middleware.plugins.configureSerialization
import io.lb.middleware.plugins.configureSession

/**
 * Main function of the server.
 */
fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

/**
 * Application module configuration.
 */
fun Application.module() {
    configureSerialization()
    configureMonitoring()
    configureSession()
    configureAuth()
    routes()
}
