package ru.practicum.shareit.requests;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByRequesterIdOrderByIdAsc(long requester_id);

    @Query(nativeQuery = true, value = "WITH temp AS " +
        "(SELECT id, description, requester_id, created, ROW_NUMBER() OVER (ORDER BY created) AS line_number " +
        "FROM item_requests " +
        "WHERE requester_id != ?) " +
        "SELECT * FROM temp WHERE line_number >= ? " +
        "LIMIT ?;")
    List<ItemRequest> findOthersRequests(long user, int from, int size);

    Page<ItemRequest> findByIdIsNot(long requesterId, Pageable pageable);
}
