FROM maven:3.9.9-eclipse-temurin-21
LABEL authors="pas-zhukov"

# Временный докерфайл, в дальнейшем компиляцию уберу
COPY . .
RUN mvn clean install -DskipTests
ENTRYPOINT ["java", "-jar", "./target/EventManager-0.0.1-SNAPSHOT.jar"]