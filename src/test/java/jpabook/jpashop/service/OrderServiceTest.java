package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    @DisplayName("상품_주문_테스트")
    public void 상품주문() throws Exception {
        //given
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강가","123-123"));
        em.persist(member);

        Book book = new Book();
        book.setName("시골 JPA");
        book.setPrice(10000); // 가격
        book.setStockQuantity(10); // 재고량
        em.persist(book);

        int orderCount = 2; // 주문 수량

        //when
        final Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        final Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.ORDER, getOrder.getStatus());
    }

    @Test
    @DisplayName("주문_취소_테스트")
    public void 주문취소() throws Exception {
        //given

        //when

        //then
    }


    @Test
    @DisplayName("상품주문_재고수량초과_테스트")
    public void 상품주문_재고수량초과() throws Exception {
        //given

        //when

        //then
    }

}