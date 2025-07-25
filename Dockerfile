FROM eclipse-temurin:21-jdk as builder

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

# Image runtime
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
