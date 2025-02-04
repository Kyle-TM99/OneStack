# 빌드 단계
FROM gradle:8.12-jdk17-alpine AS build

WORKDIR /app
COPY . .
RUN gradle build --no-daemon -x test

# 실행 단계
FROM amazoncorretto:17-alpine

WORKDIR /app

# JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# static 폴더 전체를 복사
COPY --from=build /app/src/main/resources/static /app/static

ENV JAVA_OPTS="-Xms512m -Xmx512m"
ENV SERVER_PORT=8080

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]