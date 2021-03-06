package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    // Spring Data Jpa Support
    private final EntityManager em; // 생성자 주입

    public void save(Item item) { // 파라미터는 준영속 상태
        if (item.getId() == null) { // id가 없으면 신규로 데이터 생성
            em.persist(item);
        } else {
            em.merge(item); // 병합 된 객체는 영속 상태
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findALl() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

}
