package io.lb.middleware.user.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing
import io.ktor.server.sessions.clear
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import io.ktor.util.generateNonce
import io.lb.middleware.session.MiddlewareSession
import io.lb.middleware.user.data.model.LoginRequest
import io.lb.middleware.user.data.model.ProtectedUserRequest
import io.lb.middleware.user.data.model.UpdatePasswordRequest
import io.lb.middleware.user.data.model.UserCreateRequest
import io.lb.middleware.user.data.model.UserUpdateRequest
import io.lb.middleware.user.domain.useCases.UserUseCases
import io.lb.middleware.util.MiddlewareException
import java.sql.SQLException

/**
 * Extension function with routes related to user operations.
 *
 * **Routes documentations:**
 *
 * Sign up (Create user):
 * [/api/signIn](https://documenter.getpostman.com/view/28162587/2sA3JGeihC#a61f2df0-1f61-4537-b458-28b755bc9a75)
 *
 * Login (Get auth token):
 * [/api/login](https://documenter.getpostman.com/view/28162587/2sA3JGeihC#bea84de4-64d3-45c3-a2cc-35deef980dc7)
 *
 * Logout:
 * [/api/login](https://documenter.getpostman.com/view/28162587/2sA3JGeihC#dc56397a-756f-4b97-b4db-a7fad39499a8)
 *
 * Get user by UUID:
 * [/api/user](https://documenter.getpostman.com/view/28162587/2sA3JGeihC#e836f3e1-130e-48e1-8045-69d8e2c8f2b8)
 *
 * Update user:
 * [/api/updateUser](https://documenter.getpostman.com/view/28162587/2sA3JGeihC#f4555925-1043-4457-840a-b0b1cd62efd9)
 *
 * Update user's password:
 * [/api/updatePassword](https://documenter.getpostman.com/view/28162587/2sA3JGeihC#df877090-8a69-4823-8bfa-a570f74231c3)
 *
 * Delete user:
 * [/api/deleteUser](https://documenter.getpostman.com/view/28162587/2sA3JGeihC#44e9c25d-ef79-446b-8dca-be4934599061)
 */
fun Application.userRoutes(
    useCases: UserUseCases
) {
    routing {
        post("/api/signUp") {
            val user = call.receiveNullable<UserCreateRequest>() ?: run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            try {
                val userId = useCases.signUpUseCase(user)
                call.respond(HttpStatusCode.Created, userId)
            } catch (e: SQLException) {
                call.respond(HttpStatusCode.Forbidden, e.message.toString())
            } catch (e: MiddlewareException) {
                call.respond(e.code, e.message.toString())
            }
        }

        post("/api/login") {
            val request = call.receiveNullable<LoginRequest>() ?: run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            try {
                val response = useCases.loginUseCase(request.email, request.password)
                call.sessions.set(
                    MiddlewareSession(
                        clientId = response.userId,
                        sessionId = generateNonce()
                    )
                )

                call.respond(HttpStatusCode.OK, response)
            } catch (e: SQLException) {
                call.respond(HttpStatusCode.Forbidden, e.message.toString())
            } catch (e: MiddlewareException) {
                call.respond(e.code, e.message.toString())
            }
        }

        authentication {
            get("/api/user") {
                val userId = call.parameters["userId"] ?: run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                try {
                    val user = useCases.getUserByIdUseCase(userId)
                    call.respond(HttpStatusCode.OK, user.copy(password = null))
                } catch (e: SQLException) {
                    call.respond(HttpStatusCode.Forbidden, e.message.toString())
                } catch (e: MiddlewareException) {
                    call.respond(e.code, e.message.toString())
                }
            }

            put("/api/updateUser") {
                val userId = call.parameters["userId"] ?: run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@put
                }
                if (!validateSession(userId)) {
                    return@put
                }
                val user = call.receiveNullable<UserUpdateRequest>() ?: run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@put
                }

                try {
                    useCases.updateUserUseCase(userId, user)
                    call.respond(HttpStatusCode.OK, userId)
                } catch (e: SQLException) {
                    call.respond(HttpStatusCode.Forbidden, e.message.toString())
                } catch (e: MiddlewareException) {
                    call.respond(e.code, e.message.toString())
                }
            }

            put("/api/updatePassword") {
                val userId = call.parameters["userId"] ?: run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@put
                }
                if (!validateSession(userId)) {
                    return@put
                }
                val request = call.receiveNullable<UpdatePasswordRequest>() ?: run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@put
                }

                try {
                    useCases.updatePasswordUseCase(userId, request.password, request.newPassword)
                    call.respond(HttpStatusCode.OK)
                } catch (e: SQLException) {
                    call.respond(HttpStatusCode.Forbidden, e.message.toString())
                } catch (e: MiddlewareException) {
                    call.respond(e.code, e.message.toString())
                }
            }

            delete("/api/deleteUser") {
                val userId = call.parameters["userId"] ?: run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }
                if (!validateSession(userId)) {
                    return@delete
                }
                val request = call.receiveNullable<ProtectedUserRequest>() ?: run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }

                try {
                    useCases.deleteUserUseCase(userId, request.password)
                    call.sessions.clear<MiddlewareSession>()
                    call.respond(HttpStatusCode.OK)
                } catch (e: SQLException) {
                    call.respond(HttpStatusCode.Forbidden, e.message.toString())
                } catch (e: MiddlewareException) {
                    call.respond(e.code, e.message.toString())
                }
            }

            get("/api/logout") {
                call.sessions.clear<MiddlewareSession>()
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

private suspend fun RoutingContext.validateSession(userId: String): Boolean {
    val authenticatedUserId = call.sessions.get<MiddlewareSession>()?.clientId

    if (userId != authenticatedUserId) {
        call.respond(HttpStatusCode.Unauthorized, "You are not authorized to update this user.")
        return false
    }

    return true
}
