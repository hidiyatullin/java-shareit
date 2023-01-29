package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.persistence.TypedQuery;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;

    private final TestEntityManager em;

    private User user1;
    private User user2;
    private Item item1;
    private Booking booking1;

    @Autowired
    public BookingRepositoryTest(UserRepository userRepository, ItemRepository itemRepository, BookingRepository bookingRepository, TestEntityManager em) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.bookingRepository = bookingRepository;
        this.em = em;
    }

    @BeforeEach
    public void start() {
        user1 = new User(1L, "user1", "user1@email.ru");
        user2 = new User(2L, "user2", "user2@email.ru");
        item1 = new Item(1L, "itemName1", "descriptionItem1", true, user1, null);
        booking1 = new Booking(1L, LocalDateTime.of(2022, 12, 10, 10, 30),
                LocalDateTime.of(2022, 12, 18, 10, 30),
                item1, user2, Status.APPROVED, true);

        userRepository.save(user1);
        userRepository.save(user2);
        itemRepository.save(item1);
        bookingRepository.save(booking1);
    }

    @Test
    public void findAllByBookerIdOrderByStartDesc() {
        TypedQuery<Booking> query = em.getEntityManager()
                .createQuery("Select b from Booking b where b.booker.id = :idBooker order by b.start desc ", Booking.class);
        Booking booking = query.setParameter("idBooker", booking1.getBooker().getId()).getSingleResult();
        assertNotNull(booking);
    }

    @Test
    public void findAllByBookerIdAndEndBeforeOrderByIdDesc() {
        TypedQuery<Booking> query = em.getEntityManager()
                .createQuery("Select b from Booking b where b.booker.id = :idBooker and b.end < :currentDate order by b.id desc ", Booking.class);
        query.setParameter("idBooker", booking1.getBooker().getId());
        query.setParameter("currentDate", LocalDateTime.of(2022, 12, 31, 11, 00));
        Booking booking = query.getSingleResult();
        assertNotNull(booking);
    }

    @Test
    public void findAllByBookerCurrent() {
        TypedQuery<Booking> query = em.getEntityManager()
                .createQuery("Select b from Booking b where b.booker.id = :idBooker and b.end > :currentDate order by b.id desc ", Booking.class);
        query.setParameter("idBooker", booking1.getBooker().getId());
        query.setParameter("currentDate", LocalDateTime.of(2022, 12, 15, 10, 00));
        Booking booking = query.getSingleResult();
        assertNotNull(booking);
    }
}
