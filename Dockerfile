FROM openjdk:slim

WORKDIR /opt/app-root
COPY ./battleagent/target/battleagent-1.0-SNAPSHOT.jar /opt/app-root

EXPOSE 8080 9191 9190
CMD ["java", "-jar", "battleagent-1.0-SNAPSHOT.jar"]
