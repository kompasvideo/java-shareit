package ru.practicum.shareit.requests;

import org.apache.coyote.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByRequesterIdOrderByIdAsc(long requester_id);
    @Query(nativeQuery = true, value = "WITH temp AS " +
        "(SELECT id, description, requester_id, created, ROW_NUMBER() OVER (ORDER BY created) AS line_number " +
        "FROM item_requests" +
        " WHERE requester_id != ?) " +
        "SELECT * FROM temp WHERE line_number >= ? " +
        "LIMIT ?;")
    List<ItemRequest> findOthersRequests(long user, int from, int size);
}
