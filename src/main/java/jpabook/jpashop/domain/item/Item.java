package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy =InheritanceType.SINGLE_TABLE )  // 상속관계 매핑 -> 상속관계 전략 지정, 'SINGLE_TABLE'
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price; // 가격

    private int stockQuantity; // 재고량

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //  DDD -> 비지니스 로직을 엔티티내에 처리

    /*
    * stock 증가
    * */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /*
    * stock 감소
    * */
    public void removeStock(int quantity) {
        final int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }

}
