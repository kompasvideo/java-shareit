package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.Status;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByEndDesc(Long bookedId);

    List<Booking> findAllByBookerIdAndStatusOrderByEndDesc(Long bookerId, Status status);

    @Query("select b from Booking as b" +
        " join Item as i on i.id = b.item.id" +
        " where i.ownerId = :ownerId" +
        " order by b.end desc ")
    List<Booking> findAllByOwnerIdOrderByEndDesc(@Param("ownerId") Long ownerId);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long userId, Status waiting);

    @Query(value = "SELECT * from BOOKINGS b " +
        " JOIN ITEMS I ON I.ID = b.ITEM_ID" +
        " WHERE I.ID = :itemId AND I.OWNER_ID =:ownerId AND b.STATUS = 'APPROVED'" +
        " ORDER BY b.END_DATE_TIME " +
        " LIMIT 2",
        nativeQuery = true)
    List<Booking> findTwoBookingByOwnerIdOrderByEndAsc(@Param("ownerId") Long ownerId, @Param("itemId") Long itemId);

    List<Booking> findByBookerIdAndItemId(Long bookerId, Long itemId);

}
