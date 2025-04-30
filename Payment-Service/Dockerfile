FROM openjdk:17

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} paymentserver.jar

ENTRYPOINT ["java", "-jar", "/paymentserver.jar"]

EXPOSE 8081