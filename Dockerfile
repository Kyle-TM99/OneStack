# 빌드 단계
FROM gradle:8.12-jdk17-alpine AS build

WORKDIR /app

# 의존성 먼저 복사 및 다운로드
COPY build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon

# 소스 복사 및 빌드
COPY src ./src
RUN gradle build --no-daemon -x test

# 실행 단계
FROM amazoncorretto:17-alpine

WORKDIR /app

# 정적 리소스 디렉토리 생성
RUN mkdir -p /app/src/main/resources/static/images

# JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 이미지 파일들 복사
COPY --from=build /app/src/main/resources/static/images/ /app/src/main/resources/static/images/

ENV JAVA_OPTS="-Xms512m -Xmx512m"
ENV SERVER_PORT=8080

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]