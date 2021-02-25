FROM openjdk:11-jdk
WORKDIR /app
COPY . .
RUN ./gradlew build
RUN cp build/libs/*.jar alimi.jar

ENTRYPOINT ["java","-jar","alimi.jar"]