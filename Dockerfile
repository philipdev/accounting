FROM node:18 as npm
WORKDIR /workspace/angular-client

COPY ./angular-client /workspace/angular-client
COPY ./angular-client/src/assets/prod-config.json /workspace/angular-client/src/assets/config.json

RUN npm install 
RUN ./node_modules/.bin/ng build

FROM eclipse-temurin:17-jdk-alpine as jdk

WORKDIR /workspace

COPY ./gradle /workspace/gradle
COPY ./gradlew /workspace/gradlew
COPY ./settings.gradle /workspace/settings.gradle
COPY ./build.gradle /workspace/build.gradle
COPY ./server /workspace/server


RUN --mount=type=cache,target=/root/.gradle ./gradlew build
#RUN jar -xf /workspace/server/build/libs/server-0.0.1-SNAPSHOT.jar


FROM eclipse-temurin:17-jre-alpine

ARG DB_USER
ARG DB_SCHEMA
ARG DB_PASS
ARG DB_SCHEMA

WORKDIR /run
COPY --from=npm /workspace/angular-client/dist/angular-client /run/angular-client
COPY --from=jdk /workspace/server/build/libs/server-0.0.1-SNAPSHOT.jar /run/server-0.0.1-SNAPSHOT.jar
EXPOSE 8080

ENTRYPOINT ["java","-Dspring.web.resources.static-locations=file:/run/angular-client", "-jar","/run/server-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]