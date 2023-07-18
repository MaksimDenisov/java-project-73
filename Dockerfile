FROM gradle:7.6-jdk17

WORKDIR /app

COPY /app .

RUN gradle build

CMD java -jar ./build/libs/app-1.0-SNAPSHOT.jar
