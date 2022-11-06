FROM openjdk:8-jdk-alpine
# 使用非root用户
RUN addgroup -S xdj && adduser -S xdj -G xdj
USER xdj:xdj

# fat-jar
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

# layers
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.xiaodaojiang.containerimage.ContainerImageApplication"]