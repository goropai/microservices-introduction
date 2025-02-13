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

COPY --from=build /app/target/*.jar api-gateway.jar
COPY --from=build /app/target/*.jar ./lib/

EXPOSE 8765
CMD ["java","-jar","api-gateway.jar"]