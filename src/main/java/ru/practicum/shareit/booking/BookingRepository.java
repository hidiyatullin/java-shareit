package ru.practicum.shareit.booking;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId, PageRequest pageRequest);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByIdDesc(Long userId, LocalDateTime now, PageRequest pageRequest);

    @Query(nativeQuery = true,
            value = "select * from BOOKINGS as b" +
                    " where b.BOOKER_ID = ?1 and ?2 > b.START_DATE" +
                    "  and ?2 < b.END_DATE order by b.START_DATE ")
    List<Booking> findAllByBookerCurrent(Long userId, LocalDateTime now, PageRequest pageRequest);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime now, PageRequest pageRequest);

    @Query(nativeQuery = true,
            value = "select * from BOOKINGS as b " +
                    " where b.BOOKER_ID = ?1 and b.STATUS = 'WAITING' " +
                    "order by b.START_DATE desc ")
    List<Booking> findAllByBookerIdStateWaiting(Long userId, PageRequest pageRequest);

    @Query(nativeQuery = true,
            value = "select * from BOOKINGS as b " +
                    " where b.BOOKER_ID = ?1 and b.STATUS = 'REJECTED' " +
                    "order by b.START_DATE desc ")
    List<Booking> findAllByBookerIdStateRejected(Long userId, PageRequest pageRequest);


    @Query(nativeQuery = true,
            value = "select * from BOOKINGS as b " +
                    "join ITEMS i on i.ID = b.ITEM_ID " +
                    " where i.OWNER_ID = ?1 " +
                    "order by b.START_DATE desc ")
    List<Booking> findAllByOwnerIdOrderByStartDesc(Long userId, PageRequest pageRequest);

    @Query(nativeQuery = true,
            value = "select * from BOOKINGS as b " +
                    "join ITEMS i on i.ID = b.ITEM_ID " +
                    " where i.OWNER_ID = ?1 and b.START_DATE<?2 and b.END_DATE>?2 " +
                    "order by b.START_DATE desc ")
    List<Booking> findAllByOwnerIdCurrent(Long userId, LocalDateTime now, PageRequest pageRequest);

    @Query(nativeQuery = true,
            value = "select * from BOOKINGS as b " +
                    "join ITEMS i on i.ID = b.ITEM_ID " +
                    " where i.OWNER_ID = ?1 and b.START_DATE<?2 and b.END_DATE<?2 " +
                    "order by b.START_DATE desc ")
    List<Booking> findAllByOwnerIdStatePast(Long userId, LocalDateTime now, PageRequest pageRequest);

    @Query(nativeQuery = true,
            value = "select * from BOOKINGS as b " +
                    "join ITEMS i on i.ID = b.ITEM_ID " +
                    " where i.OWNER_ID = ?1 and b.START_DATE>?2 and b.END_DATE>?2 " +
                    "order by b.START_DATE desc ")
    List<Booking> findAllByOwnerIdStateFuture(Long userId, LocalDateTime now, PageRequest pageRequest);

    @Query(nativeQuery = true,
            value = "select * from BOOKINGS as b " +
                    "join ITEMS i on i.ID = b.ITEM_ID " +
                    " where i.OWNER_ID = ?1 and b.STATUS = ?2 " +
                    "order by b.START_DATE desc ")
    List<Booking> findAllByOwnerIdState(Long userId, String status, PageRequest pageRequest);

    Booking findFirstByItemIdAndEndBeforeOrderByEndDesc(Long itemId, LocalDateTime end);

    Booking findOneByItemIdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime start);
}