package ru.practicum.shareit.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query(nativeQuery = true,
            value = "select * from REQUESTS as r " +
                    "where r.REQUESTER_ID = ?1 " +
                    "order by r.CREATED desc ")
    List<ItemRequest> findByUserId(Long userId);

    @Query(nativeQuery = true,
            value = "select * from REQUESTS as r " +
                    "join ITEMS i on r.ID = i.REQUEST_ID " +
                    "where r.REQUESTER_ID != ?1 " +
                    "order by r.CREATED desc ")
    List<ItemRequest> findAllByUserID(Long userId, PageRequest pageRequest);
}