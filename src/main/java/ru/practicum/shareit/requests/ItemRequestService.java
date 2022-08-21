package ru.practicum.shareit.requests;

import ru.practicum.shareit.booking.item.model.Item;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestInputDto;

import java.util.List;
import java.util.NoSuchElementException;

public interface ItemRequestService {
    ItemRequestDto addNewRequest(long userId, ItemRequestInputDto itemRequestInputDto) throws Throwable;
    List<ItemRequestDto> getListRequest(long userId) throws Throwable;
    List<ItemRequestDto> getListRequestAllUsers(long userId, int from, int size);
    ItemRequestDto getOneRequest(long userId, long requestId) throws Throwable;
    void addResponse(Item item, long requestId);
    ItemRequest getItemRequestById(long itemRequestId) throws NoSuchElementException;
}
