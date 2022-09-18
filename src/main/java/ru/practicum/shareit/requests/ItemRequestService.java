package ru.practicum.shareit.requests;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestInputDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;
import java.util.NoSuchElementException;

public interface ItemRequestService {
    ItemRequestDto addNewRequest(long userId, ItemRequestInputDto itemRequestInputDto);

    List<ItemRequestDto> getListRequest(long userId);

    List<ItemRequestDto> getListRequestAllUsers(long userId, int from, int size);

    ItemRequestDto getOneRequest(long userId, long requestId);

    void responsesAddItems(Item item, long requestId);

    ItemRequest findById(long itemRequestId) throws NoSuchElementException;
}
