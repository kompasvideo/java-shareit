package ru.practicum.shareit.booking.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.item.dto.CommentDto;
import ru.practicum.shareit.booking.item.model.Comment;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final CommentRepository commentRepository;

    public static CommentDto toCommentDto(Comment comment, String authorName) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(authorName);
        commentDto.setCreated(comment.getCreated());
        return commentDto;
    }
}
