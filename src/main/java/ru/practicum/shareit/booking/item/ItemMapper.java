package ru.practicum.shareit.booking.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.item.dto.CommentDto;
import ru.practicum.shareit.booking.item.dto.ItemDto;
import ru.practicum.shareit.booking.item.dto.OwnerItemDto;
import ru.practicum.shareit.booking.item.model.Item;

import java.util.List;

//@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
@RequiredArgsConstructor
@Slf4j
public class ItemMapper {

    private static ModelMapper modelMapper;


    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
            .id(item.getId())
            .name(item.getName())
            .description(item.getDescription())
            .available(item.getAvailable())
            .build();
    }

    public static OwnerItemDto toItemFoundDto(Item item,
                                              OwnerItemDto.LastBooking lastBooking,
                                              OwnerItemDto.NextBooking nextBooking,
                                              List<CommentDto> commentsDto) {
        OwnerItemDto itemDto = new OwnerItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setComments(commentsDto);
        if (lastBooking != null) {
            itemDto.setLastBooking(lastBooking);
        }
        if (nextBooking != null) {
            itemDto.setNextBooking(nextBooking);
        }
        return itemDto;
    }

    public static OwnerItemDto toItemFoundDto(Item item,
                                              OwnerItemDto.LastBooking lastBooking,
                                              OwnerItemDto.NextBooking nextBooking) {
        OwnerItemDto itemDto = new OwnerItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        if (lastBooking != null) {
            itemDto.setLastBooking(lastBooking);
        }
        if (nextBooking != null) {
            itemDto.setNextBooking(nextBooking);
        }
        return itemDto;
    }
}


