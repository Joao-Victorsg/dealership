FROM alpine:3.21.3

ENV tz=gmt-3

RUN apk --no-cache --update add openjdk21  \
  && apk update && apk upgrade

COPY target/car.api-0.4.0-SNAPSHOT.jar car-api.jar

EXPOSE 8086

ENTRYPOINT ["sh","-c","java -jar /car-api.jar"]