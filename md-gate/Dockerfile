# 공식 Java  이미지 사용
FROM openjdk:18

# 환경 변수 설정
ENV TZ=Asia/Seoul
ENV APP_HOME=/usr/app/

# 현재 디렉토리 내용을 /usr/app의 컨테이너에 복사합니다.
WORKDIR $APP_HOME
COPY target/md-gate-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar
# COPY src/main/resources/application.json .

# 네트워크 통신을 위한 포트 노출 (예시)
EXPOSE 9999

# 컨테이너가 시작될 때 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar", "run", "com.mist.Gate", "-conf"]
