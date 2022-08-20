package ru.practicum.shareit.requests;

import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestInputDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addNewRequest(long userId, ItemRequestInputDto itemRequestInputDto) throws Throwable;
    List<ItemRequestDto> getListRequest(long userId) throws Throwable;
    List<ItemRequest> getListRequestAllUsers(long userId, long from, long size);
    ItemRequest getOneRequest(long userId, long requestId);
}
