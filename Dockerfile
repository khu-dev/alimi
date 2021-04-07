FROM openjdk:11-jdk
WORKDIR /khumu
COPY . .
RUN ./gradlew build  --exclude-task test --exclude-task testClasses

#RUN cp build/libs/*.jar alimi.jar && \
#    jar -xf alimi.jar
# 단순히 jar만이 아니라 -xf로 jar을 풀어줘야
# classpath를 올바르게 사용할 수 있더라 우선은 그 정도.
# 근데 properties에서 include 말고 active profile을 이용하니까 괜찮아진 것 같기도........

#ENTRYPOINT ["java", "-classpath", "BOOT-INF/classes:BOOT-INF/lib/*:/khumu/src/main/resources", "com.khumu.alimi.AlimiApplication"]
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "build/libs/*.jar"]