FROM openjdk:8-jre-alpine
COPY target/Quickr-0.0.1-SNAPSHOT.jar /Quickr-0.0.1-SNAPSHOT.jar
CMD java -jar /Quickr-0.0.1-SNAPSHOT.jar