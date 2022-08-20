package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestInputDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService{
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public ItemRequestDto addNewRequest(long userId, ItemRequestInputDto itemRequestInputDto) throws Throwable {
        User user = userRepository.findById(userId).orElseThrow(Throwable::new);
        if(itemRequestInputDto.getDescription()== null || itemRequestInputDto.getDescription().isEmpty()){
            throw new ValidationException("Поле description пустое");
        }
        ItemRequest itemRequest = modelMapper.map( itemRequestInputDto, ItemRequest.class);
        itemRequest.setRequester(user);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequestRepository.save(itemRequest);
        return  modelMapper.map(itemRequest, ItemRequestDto.class);
    }

    @Override
    public List<ItemRequestDto> getListRequest(long userId) throws Throwable {
        User user = userRepository.findById(userId).orElseThrow(Throwable::new);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterIdOrderByIdAsc(user.getId());
        List<ItemRequestDto> itemRequestDto = new ArrayList<>();
        for (ItemRequest itemRequest: itemRequests ) {
            itemRequestDto.add(modelMapper.map(itemRequest, ItemRequestDto.class));
        }
        return itemRequestDto;
    }

    @Override
    public List<ItemRequest> getListRequestAllUsers(long userId, long from, long size) {
        return null;
    }

    @Override
    public ItemRequest getOneRequest(long userId, long requestId) {
        return null;
    }
}

