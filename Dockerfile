FROM openjdk:11-jdk
WORKDIR /khumu
COPY . .
RUN ./gradlew build  --exclude-task test --exclude-task testClasses

RUN cp build/libs/*.jar alimi.jar
# build할 때 이용한 properties만 사용하는 것이 아니라 동적으로 사용 가능한 properties
ENTRYPOINT ["java", "-Dspring.config.location=src/main/resources/application.properties", "-jar","alimi.jar"]