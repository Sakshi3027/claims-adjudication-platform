# Multi-stage build: compile with Maven, run on a slim JRE.
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
COPY rules-engine/pom.xml rules-engine/pom.xml
COPY audit/pom.xml audit/pom.xml
COPY api/pom.xml api/pom.xml
COPY bdd-tests/pom.xml bdd-tests/pom.xml
# Pull dependencies first so this layer caches even when source changes
RUN mvn -q -pl rules-engine,audit,api -am dependency:go-offline || true
COPY rules-engine rules-engine
COPY audit audit
COPY api api
RUN mvn -q -pl rules-engine,audit,api -am package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /build/api/target/api-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
