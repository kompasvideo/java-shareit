package ru.practicum.shareit.booking.item.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

/**
 * // Класс вещь
 */
@Entity
@Getter
@Setter
@ToString
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Boolean available; // статус о том, доступна или нет вещь для аренды

    @Column(name = "owner_id")
    private Long ownerId; // id владелец

    @Column(name = "request_id")
    private Long requestId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id)
            && Objects.equals(name, item.name)
            && Objects.equals(description, item.description)
            && Objects.equals(available, item.available)
            && Objects.equals(ownerId, item.ownerId)
            && Objects.equals(requestId, item.requestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, available, ownerId, requestId);
    }
}
