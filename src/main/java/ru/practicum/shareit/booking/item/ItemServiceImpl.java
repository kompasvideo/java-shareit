package ru.practicum.shareit.booking.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.item.model.Item;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final ItemRepository repository;
    @Override
    public Item saveItem(long userId, Item item) {
        return repository.save( userId,item);
    }

    @Override
    public Item updateItem(long userId, long itemId, Item item){
        return repository.updateItem(userId, itemId, item);
    }

    @Override
    public Item getItem(long userId, long itemId){
        return repository.getItem(userId, itemId);
    }

    @Override
    public List<Item> getAllItem(long userId){
        return repository.getAllItem(userId);
    }

    @Override
    public List<Item> searchItem(long userId, String text){
        return repository.searchItem( userId, text);
    }
}
