# Build
FROM maven:3.8-openjdk-17-slim AS builder
COPY src /home/botto/src
COPY pom.xml /home/botto
RUN mvn -f /home/botto/pom.xml clean package

# Bottowitzsch Container
FROM openjdk:jdk-slim-buster
COPY --from=builder /home/botto/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
CMD ["--token=${PUB_TOKEN}"]