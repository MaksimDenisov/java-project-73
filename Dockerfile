FROM gradle:7.6-jdk17

WORKDIR /

COPY / .

RUN gradle build

CMD java -jar ./build/libs/app-1.0-SNAPSHOT.jar
