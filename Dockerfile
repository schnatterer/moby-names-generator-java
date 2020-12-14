FROM adoptopenjdk/openjdk11:jdk-11.0.9.1_1-alpine-slim as jdk

FROM jdk as maven-cache
ENV MAVEN_OPTS=-Dmaven.repo.local=/mvn
WORKDIR /app
COPY .mvn/ /app/.mvn/
COPY mvnw /app/ 
COPY pom.xml /app/
COPY example-java8/pom.xml /app/example-java8/
COPY example-java9/pom.xml /app/example-java9/
COPY moby-names-generator/pom.xml /app/moby-names-generator/
COPY processor/pom.xml /app/processor/
RUN ./mvnw dependency:go-offline

FROM jdk as maven-build
ENV MAVEN_OPTS=-Dmaven.repo.local=/mvn 
COPY --from=maven-cache /mvn/ /mvn/
COPY --from=maven-cache /app/ /app
COPY example-java9 /app/example-java9
COPY moby-names-generator /app/moby-names-generator
COPY processor /app/processor
WORKDIR /app
RUN ./mvnw package -pl '!example-java8'

FROM oracle/graalvm-ce:20.3.0-java11 AS native-image
RUN gu install native-image
COPY --from=maven-build /app/example-java9/target/example-java9-*-jar-with-dependencies.jar /src/app.jar
RUN native-image -H:+ReportExceptionStackTraces --static -jar /src/app.jar

FROM scratch
COPY --from=native-image /app /app
ENTRYPOINT ["/app"]