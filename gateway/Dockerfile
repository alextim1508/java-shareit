FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar app.jar
RUN apk add --no-cache tzdata
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8081
ENTRYPOINT ["java","-jar","/app.jar"]
