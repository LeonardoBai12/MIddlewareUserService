# MiddlewareUserService

## Overview

MiddlewareUserService is a dedicated authentication microservice developed with Kotlin and Ktor that serves as the security layer for the Project Middleware ecosystem. This service manages user authentication, registration, and credential validation with robust security practices, following clean architecture principles for maintainability and scalability.

By separating authentication concerns into a dedicated microservice, the main middleware can focus exclusively on its primary task of API transformation and mapping, resulting in a more secure, modular, and maintainable system architecture.

## Official Documentation and Resources

* [Project Middleware Documentation](https://leonardos-organization-15.gitbook.io/projectmiddleware-public-docs)
* [Middleware Backend Repository](https://github.com/LeonardoBai12/ProjectMiddleware)
* [Middleware Playground App](https://github.com/LeonardoBai12/MiddlewarePlayground/)
* [Postman Documentation](https://documenter.getpostman.com/view/28162587/2sAXjRX9p1#intro)

## Key Features

* **User Management**: Complete registration and authentication flows with secure password handling
* **JWT Authentication**: Implementation of JSON Web Tokens for secure, stateless authentication
* **Password Encryption**: Industry-standard BCrypt hashing for secure credential storage
* **Validation Logic**: Comprehensive input validation for user data including email formats and password strength
* **Clean Architecture**: Clear separation of concerns with domain-driven design
* **Orthogonal Design**: Service evolves independently from the main middleware

## Security Features

* **Encrypted Password Storage**: Never stores plain text passwords, using BCrypt with dynamic salts
* **Password Strength Validation**: Enforces secure password policies (minimum length, special characters, etc.)
* **JWT Token Management**: Secure token generation, validation, and refresh mechanisms
* **Input Sanitization**: Protection against injection and other common security vulnerabilities
* **Rate Limiting**: Protection against brute force attacks and abuse

## Technologies Used

* **Kotlin**: Type-safe, concise programming language
* **Ktor**: Lightweight, flexible framework for building asynchronous servers
* **MongoDB**: NoSQL database for user data storage
* **BCrypt**: Industry-standard password hashing algorithm
* **JWT Auth**: JSON Web Token implementation for secure authentication
* **Kotlin Coroutines**: For efficient asynchronous operations
* **Kotlin Flow**: Reactive programming for handling data streams

## Architecture

The service follows Clean Architecture principles with distinct layers:

* **Domain Layer**: Contains business entities and use cases independent of external frameworks
* **Data Layer**: Implements repositories and data sources for user storage
* **Framework Layer**: Handles HTTP routes, authentication middleware, and external integrations

The service implements orthogonal design as described by Hunt and Thomas, ensuring that "changes in one component don't affect other components" - allowing the authentication service to evolve independently from the main middleware.

## API Endpoints

The MiddlewareUserService exposes several REST endpoints:

* **/signup**: Register a new user account
* **/login**: Authenticate and receive JWT tokens
* **/refresh**: Obtain a new access token using a refresh token
* **/validate**: Validate an existing token
* **/user/profile**: View and update user profile information
* **/user/password**: Change user password with security validation

## How It Integrates with Project Middleware

The MiddlewareUserService operates as a separate microservice that handles all authentication-related tasks for the Project Middleware ecosystem:

1. **User Registration**: New users register through this service
2. **Authentication**: The service validates credentials and issues JWT tokens
3. **Token Validation**: The main middleware validates tokens with this service before processing requests
4. **Security Layer**: Acts as a protective layer for all secured operations in the middleware

This separation of concerns ensures that security-related functionality can evolve independently from the core mapping and transformation logic of the middleware.
