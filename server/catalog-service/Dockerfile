# ---- build stage ----
FROM eclipse-temurin:17-jdk AS build
WORKDIR /build

# shared lives in a separate repo; Gradle resolves it from GitHub Packages.
# Pass these with: docker build --build-arg GITHUB_ACTOR=... --build-arg GITHUB_TOKEN=...
ARG GITHUB_ACTOR=""
ARG GITHUB_TOKEN=""
ENV GITHUB_ACTOR=$GITHUB_ACTOR \
    GITHUB_TOKEN=$GITHUB_TOKEN

# Build scripts first, then resolve dependencies — this layer is cached and only
# re-runs when build.gradle.kts / the version catalog change.
COPY build.gradle.kts settings.gradle.kts gradlew gradlew.bat ./
COPY gradle gradle
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

# Sources last, so code changes do not invalidate the dependency layer above.
COPY src src
RUN ./gradlew bootJar --no-daemon

# ---- run stage ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /opt/app
COPY --from=build /build/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]
