# Etapa de build
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /build

COPY services/technova-common /tmp/technova-common
RUN cd /tmp/technova-common && mvn clean install -DskipTests

WORKDIR /app

COPY services/technova-ms-user/pom.xml .
COPY services/technova-ms-user/src ./src

RUN mvn dependency:go-offline

RUN mvn clean package -DskipTests

# Etapa final de execução
FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
