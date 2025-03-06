package io.lb.middleware.core

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.lb.middleware.plugins.configureAuth
import io.lb.middleware.plugins.configureMonitoring
import io.lb.middleware.plugins.configureSerialization
import io.lb.middleware.plugins.configureSession
import io.lb.middleware.security.data.model.TokenConfig
import io.lb.middleware.user.data.repository.UserRepositoryImpl
import io.lb.middleware.user.data.service.UserDatabaseServiceImpl
import io.lb.middleware.user.domain.useCases.DeleteUserUseCase
import io.lb.middleware.user.domain.useCases.GetUserByIdUseCase
import io.lb.middleware.user.domain.useCases.GetUserByPhoneUseCase
import io.lb.middleware.user.domain.useCases.LoginUseCase
import io.lb.middleware.user.domain.useCases.SignUpUseCase
import io.lb.middleware.user.domain.useCases.UpdatePasswordUseCase
import io.lb.middleware.user.domain.useCases.UpdateUserUseCase
import io.lb.middleware.user.domain.useCases.UserUseCases

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
    val repository = UserRepositoryImpl(
        service = UserDatabaseServiceImpl()
    )
    val tokenConfig = TokenConfig.buildTokenConfig(embedded = embedded)

    val useCases = UserUseCases(
        deleteUserUseCase = DeleteUserUseCase(repository),
        getUserByIdUseCase = GetUserByIdUseCase(repository),
        getUserByPhoneUseCase = GetUserByPhoneUseCase(repository),
        loginUseCase = LoginUseCase(repository, tokenConfig),
        signUpUseCase = SignUpUseCase(repository),
        updatePasswordUseCase = UpdatePasswordUseCase(repository),
        updateUserUseCase = UpdateUserUseCase(repository),
    )

    configureSerialization()
    configureMonitoring()
    configureSession()
    configureAuth(useCases)
    routes(useCases)
}
