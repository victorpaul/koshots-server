FROM openjdk:8-jre

WORKDIR /app

ADD target/koshotserver-0.0.3.jar /app/koshotserver.jar

EXPOSE 8080

CMD ["java", "-jar", "koshotserver.jar"]
