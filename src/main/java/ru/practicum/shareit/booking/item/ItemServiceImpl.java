package ru.practicum.shareit.booking.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.item.dto.ItemDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final ItemRepository repository;
    private final ItemMapper itemMapper;
    @Override
    public ItemDto saveItem(long userId, ItemDto itemDto) {
        return itemMapper.toItemDto(repository.save( userId, itemMapper.toItem(itemDto)));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto){
        return itemMapper.toItemDto(repository.updateItem(userId, itemId, itemMapper.toItem(itemDto)));
    }

    @Override
    public ItemDto getItem(long userId, long itemId){
        return itemMapper.toItemDto(repository.getItem(userId, itemId));
    }

    @Override
    public List<ItemDto> getAllItem(long userId){
        return repository.getAllItem(userId).stream()
            .map(itemMapper::toItemDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItem(long userId, String text){
        return repository.searchItem( userId, text).stream()
            .map(itemMapper::toItemDto)
            .collect(Collectors.toList());
    }
}
