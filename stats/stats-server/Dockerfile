FROM amazoncorretto:11-alpine-jdk
COPY target/stats-server.jar stats-server.jar
ENTRYPOINT ["java","-jar","/stats-server.jar"]