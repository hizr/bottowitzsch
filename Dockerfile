FROM openjdk:jdk-slim-buster
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar", "--token=${PUB_TOKEN}"]