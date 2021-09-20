#
# Build stage
#
FROM maven:3.8.2-jdk-8-slim AS build
COPY . .
ARG ARG_MAVEN_OPTS
ENV MAVEN_OPTS=$ARG_MAVEN_OPTS
RUN echo mvn clean package $MAVEN_OPTS
RUN mvn clean package $MAVEN_OPTS

#
# Package stage
#
# FROM openjdk:8-jre-slim
# COPY --from=build app/target/*.jar /usr/local/lib/app.jar
# EXPOSE 8080 9090
# ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]

# FROM maven:3.8.2-jdk-8-slim
# COPY db-util app
# # CMD mvn -f app/pom.xml spring-boot:run
# CMD mvn -f app/pom.xml package; java -jar app/target/*jar
