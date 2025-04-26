FROM alpine:3.21.3

ENV tz=gmt-3

RUN apk --no-cache --update add openjdk21  \
  && apk update && apk upgrade

COPY target/sales-api-0.0.1-SNAPSHOT.jar sales-api.jar

EXPOSE 8086

ENTRYPOINT ["sh","-c","java -jar /sales-api.jar"]