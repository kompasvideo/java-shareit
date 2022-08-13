package ru.practicum.shareit.booking.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.item.dto.CommentDto;
import ru.practicum.shareit.booking.item.dto.ItemDto;
import ru.practicum.shareit.booking.item.dto.ItemFoundDto;
import ru.practicum.shareit.booking.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemMapper {
    public static Item toItem(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
            .id(item.getId())
            .name(item.getName())
            .description(item.getDescription())
            .available(item.getAvailable())
            .build();
    }

    public static List<ItemDto> toItemsDto(List<Item> items) {
        List<ItemDto> itemsDto = new ArrayList<>();
        items.forEach(item -> itemsDto.add(toItemDto(item)));
        return itemsDto;
    }

    public static ItemFoundDto toItemFoundDto(Item item,
                                              ItemFoundDto.LastBooking lastBooking,
                                              ItemFoundDto.NextBooking nextBooking,
                                              List<CommentDto> commentsDto) {
        ItemFoundDto itemDto = new ItemFoundDto();
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

    public static ItemFoundDto toItemFoundDto(Item item,
                                              ItemFoundDto.LastBooking lastBooking,
                                              ItemFoundDto.NextBooking nextBooking) {
        ItemFoundDto itemDto = new ItemFoundDto();
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


