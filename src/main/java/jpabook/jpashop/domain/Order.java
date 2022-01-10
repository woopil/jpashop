package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)  // N:1
    @JoinColumn(name = "member_id") // FK + Owner Setting
    private Member member;

    // FetchType.EAGER 문제점 -> N+1 이슈 (첫 쿼리의 결과 값 = N)
    // JPQL select o From order o; -> SQL select * from order;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id") // FK + Owner Setting
    private Delivery delivery;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // readOnly
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate; // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    // !! 양방향 연관관계 메서드 !! //
    // Set Order's Member set <-> Member's List<Order> add(order)
    public void setMember(Member member) {
        this.member = member;
         member.getOrders().add(this);
    }

    // Order's List<OrderItem> add(OrderItem) <-> OrderItem's Order set
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    // Order's Delivery set <-> Delivery's Order set
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // !! 다향한 파라미터를 묶는 생성 메서드 !! //
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // !! 비즈니스 로직 !! //
    /*
    * 주문 취소
    * */
    public void cancel() {
        // 상태가 배송중,배송완료
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    // !! 조회 로직 !! //
    /*
    * 전체 주문 가격 조회
    * */
    public int getTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();

    //  int totalPrice = 0;
    //  for (OrderItem orderItem : orderItems){
    //         totalPrice += orderItem.getTotalPrice();
    //  }

    //  return totalPrice;
    }
}
