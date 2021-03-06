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
         - controller
    12.회원 리포지토리 개발
        1) @PersistenceContext EntityManager 객체 생성
        2) em.persists = save(), em.find = find()
        3) JPQL 작성 >> 객체 대상 쿼리
    13.회원 서비스 개발 
        1) @Transactional(readOnly = true) > 읽기 전용 메서드 사용
        2) @RequiredArgsConstructor // final 적용된 필드의 생성자 생성 -> 생성자 주입
    14.회원 기능 테스트
        1) EntityManager em; em.flush() -> 영속성 컨텍스트 반영
        2) 예외 테스트
         - assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        3) 테스트용 설정 -> test/resources/application.yml
    15.상품 엔티티 개발(비지니스 로직 추가)
        1) DDD -> 엔티티내에 비즈니스 로직 설계
         - 메서드를 이용한 재고량 관리
        2) Custom Exception 생성 >> RuntimeException 상속 
    16.상품 리포지토리 개발
        1) em.persist(); -> 데이터 신규 생성 , em.merge(); 
    17.상품 서비스 개발
        1) save(), findAll(), findOne()
    18.주문, 주문상품 엔티티 개발
        1) Order 엔티티 생성 메서드 
         - public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {...}
        2) Order 메서드(주문 취소, 전체 주문 가격 조회)
        3) OrderItem 엔티티 생성 메서드
         - public static OrderItem createOrderItem(Item item, int orderPrice, int count) {...}
        4) OrderItem 메서드(감소된 재고량 원복, 주문상품 전체 가격 조회)
    19.주문 리포지토리 개발
        1) save(), findOne(), findAll() 
    20.주문 서비스 개발
        1) 도메인 모델 패턴: 엔티티가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용.
        2) 트랜잭션 스크립트 패턴: 서비스 계층에서 대부분의 비즈니스 로직을 처리하는 것.
        3) @NoArgsConstructor(access = AccessLevel.PROTECTED) -> 기본 생성자의 접근제어자 protected 
    21.주문 기능 테스트
        1) 상품_주문_테스트: 회원 생성 -> 상품 생성 -> 주문 수량 -> 주문 요청 => 정상 주문
        2) 상품주문_재고수량초과: 회원 생성 -> 상품 생성 -> 주문 초과 수량 -> 주문 요청 => 재고 초과로 인한 주문 실패
        3) 주문취소: 회원 생성 -> 상품 생성 -> 주문 수량 -> 주문 요청 -> 주문 취소 => 재고 원복 및 주문 취소 상태
        * 예외 처리 테스트: assertThrows(Ex.class, ()-> ... );
    22.주문 검색 기능 개발
        1) OrderSearch 필드 추가
         - JPA '동적쿼리' 고민 => 'JPA Criteria' 공식 스펙
    23.홈 화면과 레이아웃
        1) HomeController -> resources/templates/fragments/~.html -> resources/static/js,css
    24.회원 등록
        1) 회원 등록 폼 객체 
          - @NotEmpty(message = "회원 이름은 필수 입니다")
        2) 회원 등록 컨트롤러 -> 제약 조건 추가
          - ( @Valid MemberForm form ) >> valid 적용
          - BindingResult >> hasErrors() >> return formPage 
    25.회원 목록 조회
        * 엔티티는 핵심 비즈니스 로직만 가지고 있고, 화면을 위한 로직은 없어야 한다 => '폼객체 + DTO 사용'
        1) 회원 목록 화면 등록
          - 타임리프에서 '?' 를 사용하면 null을 무시한다 ex) <td th:text="${member.address?.city}"></td> 
        2) 회원 목록 컨트롤러 -> Model -> return listPage
    26.상품 등록
        1) 상품 등록 폼 객체 생성 -> 상품 등록 컨트롤러 설계
    27.상품 목록 조회
        1) 상품 목록 화면 등록
        2) 상품 목록 컨트롤러 -> Model -> return listPage
    28.상품 수정
        1) 상품 수정 화면 등록 -> 상품의 id 값이 hidden
        2) 상품 수정 폼 -> Id로 상품 조회 -> 수정 폼에 파싱 -> 수정 데이터 전달 -> em.merger()
    ** 29.변경 감지와 병합(merge) **
        * 영속 상태 - 식별자로 찾아온 객체  
        1) 준영속 엔티티: 영속성 컨텍스트가 관리하지 않는 엔티티(JPA 관여X)
          - 기존 식별자(Id)를 가지고 있는 엔티티
        2) 준영속 엔티티를 수정하는 2가지 방법
          - 변경 감지 기능 사용 -> 식별자로 findOne -> .setData(data) 보단 '엔티티에 비즈니스 메소드'
          - 병합(merge) 사용 -> persist():생성 or merge():일괄수정 null 포함 - !!비추천!!
        *) !!! 변경 감지로 데이터 수정 !!!
    30.상품 주문
        1) 상품 주문 컨트롤러 -> 상문 주문 서비스 -> 주문 폼 이동 (조회 데이터 파싱) -> 주문 요청
    31.주문 목록 검색, 취소
        1) 주문 목록 검색 컨트롤러 -> 주문 목록 조회 서비스 -> orderSearch 객체(검색 조건)
        2) 주문 취소 컨트롤러 -> 주문 취소 서비스 -> 취소된 재고량 원복