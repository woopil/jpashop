package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
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
        Member member = createMember();

        Book book = createBook("시골 JPA", 10000, 10);

        int orderCount = 2; // 주문 수량

        //when
        final Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        final Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.ORDER, getOrder.getStatus());
    }

    @Test
    @DisplayName("상품주문_재고수량초과_테스트")
    public void 상품주문_재고수량초과() throws Exception {
        //given
        final Member member = createMember();
        final Item item = createBook("시골 JPA", 10000, 10);
        int orderCount = 11; // 주문 수량

        //when, then
        assertEquals("need more stock", assertThrows(NotEnoughStockException.class,
                () -> orderService.order(member.getId(), item.getId(), orderCount)).getMessage());
    }

    @Test
    @DisplayName("주문_취소_테스트")
    public void 주문취소() throws Exception {
        //given
        final Member member = createMember();
        final Book item = createBook("시골 JPA", 10000, 10);
        int orderCount = 2; // 주문 수량
        final Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        //when
        orderService.cancelOrder(orderId);
        //then
        final Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL,getOrder.getStatus());
        assertEquals(10, item.getStockQuantity());

    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강가","123-123"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price); // 가격
        book.setStockQuantity(stockQuantity); // 재고량
        em.persist(book);
        return book;
    }

}
