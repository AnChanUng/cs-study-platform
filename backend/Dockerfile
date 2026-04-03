FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY backend/gradlew .
COPY backend/gradle gradle
COPY backend/build.gradle.kts .
COPY backend/settings.gradle.kts .
COPY backend/gradle.properties .
RUN chmod +x gradlew
COPY backend/src src
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
COPY content content
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
