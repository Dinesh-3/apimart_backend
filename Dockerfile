FROM openjdk:11 as build
WORKDIR /source
ARG JAR_FILE=scribe/target/api_mart*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM openjdk:11
WORKDIR application
COPY --from=build source/dependencies/ ./
COPY --from=build source/spring-boot-loader/ ./
COPY --from=build source/snapshot-dependencies/ ./
COPY --from=build source/application/ ./
ENTRYPOINT ["java","org.springframework.boot.loader.JarLauncher"]