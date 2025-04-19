FROM alpine:3.21.3

ENV tz=gmt-3

RUN apk --no-cache --update add openjdk21  \
  && apk update && apk upgrade

COPY target/api-client-0.2.0-SNAPSHOT.jar api-client.jar

EXPOSE 8085

ENTRYPOINT ["sh","-c","java -jar /api-client.jar"]