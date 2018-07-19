FROM openjdk:8-jre

WORKDIR /app

ADD target/koshot-0.0.1-SNAPSHOT.jar /app/koshot-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "koshot-0.0.1-SNAPSHOT.jar"]
