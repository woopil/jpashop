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
    7.도메인 모델과 테이블 설계
        1) 회원-주문 = 1:N 
        2) 주문-주문상품 = 1:N
        3) 상품-주문상품 = 1:N
        * 주문 - 주문상품 - 상품 : 주문상품 엔티티를 추가해 다대다 관계를 일대다 관계로 풀어냄.
        * 시스템적으론 회원이 주문을 하는것보다, 주문을 생성할때 회원이 필요함.
        !! 외래키(FK)가 있는 곳을 연관관계의 주인으로 정해라 -> 외래키의 관리 주체
    8.엔티티 클래스 개발1
        * @Getter는 열고 @Setter는 꼭 필요한 경우에만 사용.
        1) 추상 클래스 -> 상속 관계 맵핑  
          - @Inheritance(strategy =InheritanceType.SINGLE_TABLE ) // 전략 지정
          - @DiscriminatorColumn(name = "dtype") // 분류 컬럼 지정
        2) 클래스 상속 관계
          - @DiscriminatorValue("B") // 분류 컬럼 값 지정
        3) 연관관계 주인 설정 
          - @ManyToOne  // N:1 @JoinColumn(name = "필드명") // FK + Owner Setting
          - @OneToOne  // 1:1 @JoinColumn(name = "필드명") // FK + Owner Setting
        4) 연관관계 미러 설정
          - @OneToMany(mappedBy = "필드명")  // 1:N + ReadOnly Setting 
          - @OneToOne(mappedBy = "필드명")  // 1:1 + ReadOnly Setting
    9.엔티티 클래스 개발2
        