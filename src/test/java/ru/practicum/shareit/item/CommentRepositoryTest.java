package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
        import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
        import org.springframework.test.annotation.DirtiesContext;
        import ru.practicum.shareit.item.model.Comment;
        import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

        import javax.persistence.TypedQuery;
        import java.time.LocalDateTime;

        import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CommentRepositoryTest {

    private CommentRepository commentRepository;
    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private final TestEntityManager em;
    private Comment comment1;

    private Item item1;
    private User user1;

    @Autowired
    public CommentRepositoryTest(CommentRepository commentRepository, ItemRepository itemRepository, UserRepository userRepository, TestEntityManager em) {
        this.commentRepository = commentRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.em = em;
    }

    @BeforeEach
    public void start() {
        user1 = new User(1L, "user1", "user1@email.ru");
        item1 = new Item(1L, "itemName1", "descriptionItem1", true, user1, null);
        comment1 = new Comment(1L, "textComment1", item1, user1, LocalDateTime.now());
        userRepository.save(user1);
        itemRepository.save(item1);
        commentRepository.save(comment1);
    }

    @Test
    public void findAllByItemId() {
        TypedQuery<Comment> query = em.getEntityManager()
                .createQuery("Select c from Comment c join Item i on c.item.id = i.id where c.item.id = :itemId", Comment.class);
        Comment comment = query.setParameter("itemId", comment1.getId()).getSingleResult();
        assertNotNull(comment);
    }
}