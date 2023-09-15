# 베이스 이미지 선택
FROM amazoncorretto:11

# /deploy 디렉터리 생성
RUN mkdir /deploy

# JAR 파일 복사
COPY build/libs/toy-mini-blog-back.jar /deploy/

# 서버 포트 설정
EXPOSE 8081

# jar 파일 실행
ENTRYPOINT ["java","-jar","/deploy/toy-mini-blog-back.jar"]