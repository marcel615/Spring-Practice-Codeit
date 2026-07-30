# ------1. build 스테이지
FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace

# 의존성 먼저: 빌드 스크립트/래퍼만 복사해 의존성 다운로드를 캐시 레이어로 고정
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

# 소스 복사 후 실행 가능 jar 빌드 (테스트는 CI 에서, 이미지 빌드에선 제외)
COPY src ./src
RUN ./gradlew bootJar -x test

# ---- ② run 스테이지 ----
FROM eclipse-temurin:17-jre
WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*

COPY --from=build /workspace/build/libs/sprintlog-boot-0.0.1-SNAPSHOT.jar app.jar

# 타임존 설정
ENV TZ=Asia/Seoul

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080 9090

# Actuator 헬스체크(별도 포트 9090, base-path /management). 부팅 시간 고려해 start-period 여유.
HEALTHCHECK --interval=15s --timeout=3s --start-period=60s --retries=5 \
  CMD curl -fsS http://localhost:9090/management/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]