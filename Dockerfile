FROM openjdk:8-jre-alpine
MAINTAINER ekatherine.net
COPY build/libs/*.jar ./app.jar
COPY build/resources/main/* ./resources/
CMD ["/usr/bin/java", "-jar", "/app.jar", \
"--spring.profiles.active=${AGGREGATOR_ACTIVE_PROFILE}", \
"--spring.config.location=${AGGREGATOR_CONFIG_LOCATION}", \
"--server.port=${AGGREGATOR_CONTAINER_PORT}" ]
EXPOSE ${AGGREGATOR_CONTAINER_PORT}