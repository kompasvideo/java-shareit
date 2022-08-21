package ru.practicum.shareit.booking.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select it from Item as it" +
        " where (upper(it.name) like concat('%', upper(:text), '%' ) " +
        " or upper(it.description) like concat('%', upper(:text), '%' ))" +
        " and it.available = true")
    List<Item> findItemsByText(@Param("text") String text);

    List<Item> findItemsByOwnerId(long userId);

    List<Item> findAllByOwnerId(long userId);
}
