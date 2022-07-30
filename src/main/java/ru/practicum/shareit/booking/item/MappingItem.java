package ru.practicum.shareit.booking.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.item.dto.ItemDto;
import ru.practicum.shareit.booking.item.model.Item;

@Service
public class MappingItem {
    public ItemDto mapToItemDto(Item item){
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setUserId(item.getUserId());
        return itemDto;
    }

    public Item mapToItem(ItemDto dto){
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setUserId(dto.getUserId());
        return item;
    }
}
