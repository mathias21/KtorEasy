FROM gradle:7-jdk11 AS build

RUN mkdir /appbuild
COPY . /appbuild

WORKDIR /appbuild

RUN ./gradlew clean build

FROM openjdk:11

ENV APPLICATION_USER 1033
RUN useradd -ms /bin/bash $APPLICATION_USER

RUN mkdir /app
RUN mkdir /app/resources
RUN chown -R $APPLICATION_USER /app
RUN chmod -R 755 /app

USER $APPLICATION_USER

COPY --from=build /appbuild/build/libs/KtorEasy.jar /app/KtorEasy.jar
COPY --from=build /appbuild/src/main/resources/ /app/resources/
WORKDIR /app
ENTRYPOINT ["java","-jar","/app/KtorEasy.jar"]