package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.Exeption.IncorrectStatusOfItemException;
import ru.practicum.shareit.Exeption.IncorrectUserOfItemException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
@Slf4j
public class ItemRepository {

    HashMap<Long, Item> items = new HashMap<>();
    private long id = 0;
    public Item save(Item item) {
        if (item.getAvailable() == null) {
            throw new IncorrectStatusOfItemException("Неверный статус вещи");
        } else {
            id++;
            item.setId(id);
            items.put(id, item);
            log.info("Создан объект " + item + "с id  " + id);
            return items.get(id);
        }
    }

    public Item update(Item item, long itemId, long userId) {
        log.info("Обновляем объект " + itemId + "с пользователем " + userId);
        Item oldItem = items.get(itemId);
        if(oldItem.getOwner().getId() == userId) {
            if (item.getName() != null) {
                oldItem.setName(item.getName());
            } if (item.getDescription() != null) {
                oldItem.setDescription(item.getDescription());
            } if (item.getAvailable() != null) {
                oldItem.setAvailable(item.getAvailable());
            }
            return oldItem;
        } else {
            throw new IncorrectUserOfItemException("Неверный пользователь");
        }
     }


    public Item get(Long itemId) {
        return items.get(itemId);
    }

    public List<Item> findByUser(User owner) {
        List<Item> listOfItems = new ArrayList<>();
        for (Item item : items.values()) {
            if(item.getOwner().getId() == owner.getId()) {
                listOfItems.add(item);
            }
        }
        log.info("Найден список вещей по пользователю: " + listOfItems);
        return listOfItems;
    }

    public Set<Item> findItems(String text) {
        Set<Item> foundItems = new HashSet<>();
        for(Item item : items.values()) {
            if(item.getName().toLowerCase().contains(text.toLowerCase()) && !text.isBlank()) {
                if(item.getAvailable()) {
                    foundItems.add(item);
                }
            }
            if(item.getDescription().toLowerCase().contains(text.toLowerCase()) && !text.isBlank()) {
                if(item.getAvailable()) {
                    foundItems.add(item);
                }
            }
        }
        return foundItems;
    }
}
