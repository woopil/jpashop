package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional // 병합(merge) 사용
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    /*
    * 영속성 컨텍스트가 자동 변경, 리팩토링
    * */
    @Transactional // 영속 상태-변경 감지 사용
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        final Item findItem = itemRepository.findOne(itemId);

//        findItem.setName(name);
//        findItem.setPrice(price);
//        findItem.setStockQuantity(stockQuantity);

        findItem.change(name,price,stockQuantity);
    }

    public List<Item> findItems() {
        return itemRepository.findALl();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}
