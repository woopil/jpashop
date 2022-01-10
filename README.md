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
    9.엔티티 클래스 개발2 - !!! N:N 연관관계는 금지!!!
        1) N:N 연관관계 설정 >> Owner >> Category Entity
          - 다대다 연관관계 설정 시 연관테이블 설정 필수
          - @ManyToMany // N:N는 중간 테이블 생성
            @JoinTable(name = "category_item",  // 중간 테이블명
            joinColumns = @JoinColumn(name = "category_id"), // FK
            inverseJoinColumns = @JoinColumn(name = "item_id")) // FK
        2) N:N 연관관계 설정 >> ReadOnly >> Item Entity
          - @ManyToMany(mappedBy = "items")
        3) 계층 구조, 셀프 양방향 관계 설정 >> Category Entity
         - 1:N 연관관계 >> Category : List
        4) 값 타입은 변경 불가능하게 클래스 설계
         - @Embeddable + 생성자 초기화 
    10.엔티티 설계시 주의점
        1) 엔티티에는 가급적 Setter를 사용하지 말자.
        2) 모든 연관관계는 지연로딩으로 설정(LAZY) + fetch join 기능 사용
         - @XToOne 기본 전략이 'FetchType.EAGER' -> 'FetchType.LAZY' 로 변경
         - JPQL N + 1 이슈
        3) 컬렉션은 필드에서 초기화 하자 -> null 문제에서 안전 -> 하이버네이트 내장 컬렉션 이슈 방지
         - private List<OrderItem> orderItems = new ArrayList<>();
        4) 테이블, 컬럼명 생성 전략 -> 논리명(명시적 지정 X), 문리명(일괄 반영 XXX_name)
         - {SpringPhysicalNamingStrategy} => 필드명 그대로 사용
          - 카멜 케이스 -> 언더스코어 (memberPoint -> member_point)
          - .(점) -> _(언더스코어)
          - 대문자 -> 소문자
        5) 영속성 전이 >> (cascade = CascadeType.ALL)
         - 상위 엔티티에서 하위 엔티티로 모든 작업 전파
        6) 양방향 연관관계 메서드 >> 핵심적으로 컨트롤하는 곳에 코드 작성
         - 양방향 객체 설정 ( Order.class <-> Member.class )
    11.애플리케이션 아키텍처 - AA
        1) 계층형 구조 사용
         - Controller, Web: 웹 계층
         - Service: 비즈니스 로직, 트랜잭션 처리
         - Repository: JPA를 직접 사용하는 계층, 엔티티 매니저 사용
         - Domain: 엔티티가 모여 있는 계층, 모든 계층에서 사용
        2) 패키지 구조 > jpashop.
         - domain
         - exception
         - repository
         - service
         - web
    12.회원 리포지토리 개발
        1) @PersistenceContext EntityManager 객체 생성
        2) em.persists = save(), em.find = find()
        3) JPQL 작성 >> 객체 대상 쿼리
    13.회원 서비스 개발 
        1) @Transactional(readOnly = true) > 읽기 전용 메서드 사용
        2) @RequiredArgsConstructor // final 적용된 필드의 생성자 생성 -> 생성자 주입
        3) @RequiredArgsConstructor // final 적용된 필드의 생성자 생성 -> 생성자 주입