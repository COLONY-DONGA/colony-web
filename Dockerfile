FROM openjdk:17
ARG JAR_FILE=build/libs/web-proj-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} ./web-proj-0.0.1-SNAPSHOT.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-jar", "./web-proj-0.0.1-SNAPSHOT.jar"]
