package ru.practicum.shareit.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * // Класс запрос
 */
@Entity
@Getter
@Setter
@ToString
@Table(name = "item_requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

//    @Column(name = "requester_id")
//    private Long requesterId;
    @JoinColumn(name = "requester_id")
    @ManyToOne
    private User requester;

    private LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRequest that = (ItemRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(description, that.description) && Objects.equals(requester, that.requester) && Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, requester, created);
    }
}


