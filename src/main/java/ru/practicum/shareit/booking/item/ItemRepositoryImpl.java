package ru.practicum.shareit.booking.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.item.model.Item;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final List<Item> items = new ArrayList<>();
    private final UserService userService;
    private final long delete = 0;

    @Override
    public Item save(long userId, Item item) {
        valid(userId, item);
        item.setId(getId());
        item.setOwner(userId);
        items.add(item);
        return item;
    }

    @Override
    public Item updateItem(long userId, long itemId, Item item) {
        Item lItem = new Item();
        for (Item fItem : items) {
            if (fItem.getId() == itemId) {
                if (fItem.getOwner() == userId) {
                    if (item.getName() != null) {
                        fItem.setName(item.getName());
                    } else {
                        fItem.setName(fItem.getName());
                    }
                    if (item.getDescription() != null) {
                        fItem.setDescription(item.getDescription());
                    } else {
                        fItem.setDescription(fItem.getDescription());
                    }
                    if (item.getAvailable() != null) {
                        fItem.setAvailable(item.getAvailable());
                    } else {
                        fItem.setAvailable(fItem.getAvailable());
                    }
                    lItem = fItem;
                } else {
                    throw new NotFoundException("Не тот User");
                }
            }
        }
        return lItem;
    }

    @Override
    public Item getItem(long userId, long itemId) {
        Item lItem = new Item();
        for (Item fItem : items) {
            if (fItem.getId() == itemId) {
                if (fItem.getOwner() == userId) {
                    lItem = fItem;
                } else {
                    throw new NotFoundException("Не тот User");
                }
            }
        }
        return lItem;
    }

    @Override
    public List<Item> getAllItem(long userId) {
        List<Item> lItems = new ArrayList<>();
        for (Item fItem : items) {
            if (fItem.getOwner() == userId) {
                lItems.add(fItem);
            }
        }
        return lItems;
    }

    @Override
    public List<Item> searchItem(long userId, String text) {
        List<Item> lItems = new ArrayList<>();
        if (text.equals("")) {
            return lItems;
        }
        for (Item fItem : items) {
            if (fItem.getDescription().toLowerCase().lastIndexOf(text.toLowerCase()) >= 0) {
                lItems.add(fItem);
            }
        }
        return lItems;
    }

    private long getId() {
        long lastId = items.stream()
            .mapToLong(Item::getId)
            .max()
            .orElse(0);
        return lastId + 1 + delete;
    }

    private void valid(long userId, Item item) {
        if (!userService.checkUser(userId)) {
            throw new NotFoundException("Не найден User");
        }
        if (item.getAvailable() == null) {
            throw new ValidationException("Available не указан");
        }
        if (item.getName() == null) {
            throw new ValidationException("Name не указан");
        }
        if (item.getName().isEmpty()) {
            throw new ValidationException("Name не указан");
        }
        if (item.getDescription() == null) {
            throw new ValidationException("Description не указан");
        }
        if (item.getDescription().isEmpty()) {
            throw new ValidationException("Description не указан");

        }
    }
}
