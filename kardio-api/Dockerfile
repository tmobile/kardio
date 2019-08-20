FROM openjdk:8-jre-alpine3.7

MAINTAINER T-Mobile

WORKDIR /kardio-api

ADD config /kardio-api/config

ADD target/kardio-api.jar /kardio-api/

CMD ["java", "-jar", "kardio-api.jar"]

