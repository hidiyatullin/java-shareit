package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.persistence.TypedQuery;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRepositoryTest {
    private UserRepository userRepository;
    private ItemRepository itemRepository;

    private final TestEntityManager em;

    private User user1;
    private Item item1;

    @Autowired
    public ItemRepositoryTest(UserRepository userRepository, ItemRepository itemRepository, TestEntityManager em) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.em = em;
    }

    @BeforeEach
    public void start() {
        user1 = new User(1L, "user1", "user1@email.ru");
        item1 = new Item(1L, "itemName1", "descriptionItem1", true, user1, null);

        userRepository.save(user1);
        itemRepository.save(item1);
    }

    @Test
    public void findByDescriptionContainingIgnoreCase() {
        TypedQuery<Item> query = em.getEntityManager()
                .createQuery("Select i from Item i where i.description = :description", Item.class);
        Item itemNew = query.setParameter("description", item1.getDescription()).getSingleResult();
        assertNotNull(itemNew);
    }

    @Test
    public void findByIdRequest() {
        TypedQuery<Item> query = em.getEntityManager()
                .createQuery("Select i from Item i where i.id = :id", Item.class);
        Item itemNew = query.setParameter("id", item1.getId()).getSingleResult();
        assertNotNull(itemNew);
    }
}