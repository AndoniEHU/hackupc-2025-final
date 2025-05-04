FROM openjdk:17-jdk-slim
ENV APP_HOME=/app
WORKDIR $APP_HOME
COPY target/personalityapi-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]