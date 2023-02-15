FROM gradle:7.6.0-jdk11 AS build

COPY . /appbuild
WORKDIR /appbuild

RUN gradle clean build

FROM openjdk:11

ENV APPLICATION_USER 1033
RUN useradd -ms /bin/bash $APPLICATION_USER

COPY --from=build /appbuild/build/libs/KtorEasy.jar /app/KtorEasy.jar
COPY --from=build /appbuild/src/main/resources/ /app/resources/

RUN chown -R $APPLICATION_USER /app
RUN chmod -R 755 /app

USER $APPLICATION_USER

WORKDIR /app

ENTRYPOINT ["java","-jar","/app/KtorEasy.jar"]