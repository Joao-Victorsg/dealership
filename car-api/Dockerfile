FROM alpine:3.21.3

ENV tz=gmt-3

RUN apk --no-cache --update add openjdk21  \
  && apk update && apk upgrade

COPY target/car.api-0.6.0.jar car-api.jar

EXPOSE 8087

ENTRYPOINT ["sh","-c","java -jar /car-api.jar"]