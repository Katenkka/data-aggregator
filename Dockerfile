FROM openjdk:8-jre-alpine
MAINTAINER ekatherine.net
COPY build/libs/aggregator-0.0.1-SNAPSHOT.jar /app.jar
CMD ["/usr/bin/java", "-jar", "/app.jar", "--spring.profiles.active=${AGGREGATOR_ACTIVE_PROFILE}", "--server.port=${AGGREGATOR_CONTAINER_PORT}"]
EXPOSE ${AGGREGATOR_CONTAINER_PORT}