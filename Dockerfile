FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/serviciodisponibilidad-0.0.1.jar
COPY ${JAR_FILE} app_serviciodisponibilidad.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "/app_serviciodisponibilidad.jar"]
