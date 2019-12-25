FROM gradle:5.6.4-jdk8 as cache
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME /home/gradle/cache_home
COPY build.gradle /home/gradle/java-code/
COPY settings.gradle /home/gradle/java-code/
WORKDIR /home/gradle/java-code
RUN gradle clean build -i --stacktrace

FROM gradle:5.6.4-jdk8 as builder
COPY --from=cache /home/gradle/cache_home /home/gradle/.gradle
COPY . /usr/src/java-code/
WORKDIR /usr/src/java-code
RUN gradle bootJar -i --stacktrace

FROM openjdk:8-jre-alpine
MAINTAINER ekatherine.net
COPY --from=builder /usr/src/java-code/build/libs/*.jar ./app.jar
CMD ["/usr/bin/java", "-jar", "/app.jar", \
"--spring.profiles.active=${AGGREGATOR_ACTIVE_PROFILE}", \
"--spring.config.location=${AGGREGATOR_CONFIG_LOCATION}", \
"--server.port=${AGGREGATOR_CONTAINER_PORT}" ]
EXPOSE ${AGGREGATOR_CONTAINER_PORT}