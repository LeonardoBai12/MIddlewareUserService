FROM gradle:7.5-jdk17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean shadowJar --no-daemon

FROM openjdk:17-jdk-alpine AS runtime
WORKDIR /app
COPY --from=build /app/build/libs/io.lb.middleware-all.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]