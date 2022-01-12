package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany // N:N는 중간 테이블 생성
    @JoinTable(name = "category_item",  // 중간 테이블명 - Owner
            joinColumns = @JoinColumn(name = "category_id"), // FK
            inverseJoinColumns = @JoinColumn(name = "item_id")) // FK
    private List<Item> items = new ArrayList<>();

    // 게층 구조, 셀프 양방향 관계 설정
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    // 양방향 연관관계 메서드 //
    // Category's List<Category> child add() <-> Category's Category parent set
    public void addChild(Category child) {
        this.child.add(child); // List<Category> child
        child.setParent(this); // Category parent
    }

}
