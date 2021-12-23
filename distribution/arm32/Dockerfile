# Dockerfile for an arm32/v7 CPU (E.g. Raspberry PI 4). This is the way I host my Bottowitzsch :)
# Build Bottowitzsch
FROM arm32v7/maven:3.6.1-jdk-11-slim AS builder
COPY src /home/botto/src
COPY pom.xml /home/botto
RUN mvn -f /home/botto/pom.xml clean package

# Bottowitzsch Container
FROM arm32v7/openjdk:jre-slim
COPY --from=builder /home/botto/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
CMD ["--token=${PUB_TOKEN}"]