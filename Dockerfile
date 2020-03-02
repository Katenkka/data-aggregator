FROM openjdk:8u212-alpine
MAINTAINER ekatherine.net
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n
COPY build/libs/*.jar /application/app.jar
COPY build/resources/main/* /application/resources/
RUN cd /application
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "app.jar", \
"--spring.profiles.active=${AGGREGATOR_ACTIVE_PROFILE}", \
"--spring.config.location=${AGGREGATOR_CONFIG_LOCATION}", \
"--server.port=${AGGREGATOR_CONTAINER_PORT}" ]
EXPOSE 8000
EXPOSE ${AGGREGATOR_CONTAINER_PORT}