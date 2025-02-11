# Build stage
FROM eclipse-temurin:17-alpine AS build
WORKDIR /app

# Install Maven
RUN apk add --no-cache maven

COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar song-service.jar
COPY --from=build /app/target/*.jar ./lib/

EXPOSE 8082
CMD ["java","-jar","song-service.jar"]