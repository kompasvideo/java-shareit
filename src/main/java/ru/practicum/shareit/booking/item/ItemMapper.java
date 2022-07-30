package ru.practicum.shareit.booking.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.item.dto.ItemDto;
import ru.practicum.shareit.booking.item.model.Item;

@Service
public class ItemMapper {
    public ItemDto toItemDto(Item item){
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setOwner(item.getOwner());
        itemDto.setItemRequest(item.getItemRequest());
        return itemDto;
    }

    public Item toItem(ItemDto dto){
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setOwner(dto.getOwner());
        return item;
    }
}
