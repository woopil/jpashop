package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery") // ReadOnly, Order 클래스의 delivery 필드 지정
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING) // Enum 사용시 지정
    private DeliveryStatus status;  // READY, COMP
}
