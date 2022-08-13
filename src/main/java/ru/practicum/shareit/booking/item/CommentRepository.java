package ru.practicum.shareit.booking.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.item.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByItemId(Long itemId);
}