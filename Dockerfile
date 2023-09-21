# 베이스 이미지 선택
FROM amazoncorretto:11

# /deploy 디렉터리 생성
RUN mkdir /deploy

# JAR 파일 변수 지정
ARG JAR_FILE=build/libs/till.back-0.0.1-SNAPSHOT.jar 

ADD ${JAR_FILE} /deploy/toy-mini-blog-back.jar

# 환경 변수로 프로파일 설정
ENV SPRING_PROFILES_ACTIVE=prod

# jar 파일 실행
ENTRYPOINT ['java', '-jar', '/deploy/toy-mini-blog-back.jar']

# 서버 포트 설정
EXPOSE 8081
