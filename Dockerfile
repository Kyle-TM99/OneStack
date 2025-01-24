# 빌드 단계
FROM gradle:8.12-jdk17-alpine AS build

WORKDIR /app

# 의존성 먼저 복사 및 다운로드 (캐시 활용)
COPY build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon

# 소스 복사 및 빌드
COPY src ./src
RUN gradle build --no-daemon -x test

# 실행 단계
FROM amazoncorretto:17-alpine

WORKDIR /app

# 필요한 파일만 복사
COPY --from=build /app/build/libs/*.jar app.jar

ENV JAVA_OPTS="-Xms512m -Xmx512m"
ENV SERVER_PORT=8080

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]