FROM alpine:3.21.3

ENV tz=gmt-3

RUN apk --no-cache --update add openjdk21  \
  && apk update && apk upgrade

COPY target/client-api-0.4.0-SNAPSHOT.jar client-api.jar

EXPOSE 8085

ENTRYPOINT ["sh","-c","java -jar /client-api.jar"]