package io.lb.middleware.core

import io.ktor.server.application.Application
import io.lb.middleware.model.TokenConfig
import io.lb.middleware.user.data.repository.UserRepositoryImpl
import io.lb.middleware.user.data.service.UserDatabaseServiceImpl
import io.lb.middleware.user.domain.useCases.DeleteUserUseCase
import io.lb.middleware.user.domain.useCases.GetUserByIdUseCase
import io.lb.middleware.user.domain.useCases.GetUserByPhoneUseCase
import io.lb.middleware.user.domain.useCases.LoginByPhoneUseCase
import io.lb.middleware.user.domain.useCases.LoginUseCase
import io.lb.middleware.user.domain.useCases.SignUpUseCase
import io.lb.middleware.user.domain.useCases.UpdatePasswordUseCase
import io.lb.middleware.user.domain.useCases.UpdateUserUseCase
import io.lb.middleware.user.domain.useCases.UserUseCases
import io.lb.middleware.user.routes.userRoutes

val embedded = false

fun Application.routes() {
    val repository = UserRepositoryImpl(
        service = UserDatabaseServiceImpl()
    )
    val tokenConfig = TokenConfig.buildTokenConfig(embedded = embedded)

    userRoutes(
        useCases = UserUseCases(
            deleteUserUseCase = DeleteUserUseCase(repository),
            getUserByIdUseCase = GetUserByIdUseCase(repository),
            getUserByPhoneUseCase = GetUserByPhoneUseCase(repository),
            loginUseCase = LoginUseCase(repository, tokenConfig),
            loginByPhoneUseCase = LoginByPhoneUseCase(repository, tokenConfig),
            signUpUseCase = SignUpUseCase(repository),
            updatePasswordUseCase = UpdatePasswordUseCase(repository),
            updateUserUseCase = UpdateUserUseCase(repository),
        )
    )
}
