FROM openjdk:17

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} orderserver.jar

ENTRYPOINT ["java", "-jar", "/orderserver.jar"]

EXPOSE 8082