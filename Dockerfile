FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /workspace

COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw -B dependency:go-offline

COPY src src
RUN ./mvnw -B clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

RUN groupadd --system spring && useradd --system --gid spring spring

COPY --from=build /workspace/target/*.jar app.jar

USER spring:spring

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
