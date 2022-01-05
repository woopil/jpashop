# 실전! 스프링 부트와 JPA 활용1
    1.프로젝트 생성 -> start.spring.io
    2.라이브러리 살펴보기
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
    5.JPA와 DB 설정
        1) properties -> yml (datasource, jpa, logging)
          - docs.spring.io/spring-boot/docs/current/reference/html
        2) Member 등록
          - Entity: 필드 생성 
          - Repository: @PersistenceContext -> EntityManager -> CRUD 메소드 작성 
        3) Test Code 작성
          - @SpringBootTest >> @Test, @Transactional, @Rollback(value = false)
          - 같은 Transactional 안에서 키값이 같을 경우엔 같은 엔티티로 인식 (영속성 컨텍스트-1차캐시)
    6.요구사항 분석
        1) 회원 기능 - 회원가입, 회원목록
        2) 상품 기능 - 상품등록, 상품수정, 상품목록
        3) 주문 기능 - 상품주문, 주문내역, 주문취소
        4) 기타요구사항 - 상품 재고관리, 상품{도서,음반,영화}, 상품 주문시 배송 정보 입력
    7.

