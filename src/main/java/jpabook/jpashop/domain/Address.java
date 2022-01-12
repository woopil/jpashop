package jpabook.jpashop.domain;

import lombok.Getter;
import javax.persistence.Embeddable;

@Embeddable
@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    //Value Object(VO)
    //값 타입은 변경 불가능하게 클래스 설계
    private String city;
    private String street;
    private String zipcode;

    // JPA SPEC
    protected Address() {
    }

    // Setter 대신에 생성자로 초기화
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
