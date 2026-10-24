# ---- Stage 1: build the jar ----
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

COPY .mvn .mvn
COPY mvnw pom.xml ./
# Make sure the wrapper script has Unix line endings (it may be checked out with CRLF on Windows)
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

COPY src src
# Tests are skipped here: integration tests need a database, which is not available during the image build.
RUN --mount=type=cache,target=/root/.m2 ./mvnw -B -q package -DskipTests

# ---- Stage 2: run it on a smaller JRE image ----
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
