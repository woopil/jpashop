# 실전! 스프링 부트와 JPA 활용1
    1.프로젝트 생성
    2.라이브러리 살펴보기 - 
        - lombok: @Getter, @Setter, @Builder
        - devtools: Recompile 
    3.View 환경 설정 - thymeleaf
        1) 스프링 부트 thymeleaf viewName 매핑
          - resources>templates + (ViewName).html
    4.H2 Database 설치
        1) h2database.com >> H2 다운로드
        2) h2/bin/h2.sh >> 실행
        3) localhost:8082 >> 접속 (최초1번은 세션키 유지)
        4) JDBC URL=jdbc:h2:~/jpashop >> .db 파일 생성
        5) jdbc:h2:tcp://localhost/~/jpashop >> 네트워크 모드로 접속

