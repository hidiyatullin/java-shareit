package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
//import java.util.Set;

//public interface ItemRepository {
//    Item save(Item item);
//
//    Item update(Item item, long itemId, long userId);
//
//    Item get(Long itemId);
//
//    List<Item> findByUser(User owner);
//
//    Set<Item> searchItems(String text);
//}

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByUser(User owner);

    @Query(" select i from Item i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%'))")
    List<Item> searchItems(String text);
}