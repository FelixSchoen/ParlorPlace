FROM openjdk:16.0.2-jdk-oracle
MAINTAINER fschoen.com
ARG JAR_FILE=*.jar
ARG TEST_DATA=--test.data
COPY ${JAR_FILE} ppbackend.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=setup", "/ppbackend.jar", "--test.data"]