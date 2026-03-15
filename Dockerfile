# Multi-stage Docker build for Selenium tests

# Build stage
FROM maven:3.8.1-openjdk-11 as builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:11-jre-slim
WORKDIR /app

# Install Chrome for local execution
RUN apt-get update && apt-get install -y \
    curl \
    wget \
    gnupg \
    unzip \
    && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/target ./target
COPY --from=builder /app/pom.xml .
COPY --from=builder /app/src ./src

# Copy config with remote driver mode
RUN mkdir -p src/test/resources
COPY src/test/resources/config.properties src/test/resources/

ENTRYPOINT ["mvn", "test", "-Dconfig.mode=remote"]
