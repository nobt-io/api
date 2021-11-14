FROM openjdk:8-jre-slim-buster

COPY rest-api-1.0.0/ /app

ENV PORT 8080
ENV MIGRATE_DATABASE_AT_STARTUP false
ENV USE_IN_MEMORY_DATABASE true

EXPOSE 8080

WORKDIR /app

CMD "bin/rest-api"
