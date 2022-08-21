package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.item.ItemRepository;
import ru.practicum.shareit.booking.item.model.Item;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestInputDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.print.attribute.standard.Destination;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService{
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
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
        ItemRequestDto itemRequestDto =  modelMapper.map(itemRequest, ItemRequestDto.class);
        List<ItemRequestDto.Item> responses = new ArrayList<>();
        for (Item item : itemRequest.getItems()) {
            responses.add(new ItemRequestDto.Item(item.getId(), item.getName(), item.getDescription(),
                item.getAvailable(), item.getRequest().getId()));
        }
        itemRequestDto.setItems(responses);
        return  itemRequestDto;
    }

    @Override
    public List<ItemRequestDto> getListRequest(long userId) throws Throwable {
        User user = userRepository.findById(userId).orElseThrow(Throwable::new);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterIdOrderByIdAsc(user.getId());
        for (ItemRequest itemRequest: itemRequests ) {
            List<Item> items = itemRepository.findAllByRequestId(itemRequest.getId());
            itemRequest.setItems(items);
        }
        return ItemRequestMapper.toItemRequestDtos(itemRequests);
    }

    @Override
    public List<ItemRequestDto> getListRequestAllUsers(long userId, int from, int size) {
        if (from < 0 | size == 0){
            throw new ValidationException("Индекс from или size < 1");
        }
        List<ItemRequest> requests = itemRequestRepository.findOthersRequests(userId, from, size);
        return ItemRequestMapper.toItemRequestDtos(requests);
    }

    @Override
    public ItemRequestDto getOneRequest(long userId, long requestId) throws Throwable {
        userRepository.findById(userId).orElseThrow(Throwable::new);
        itemRequestRepository.findById(requestId).orElseThrow(NotFoundException::new);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterIdOrderByIdAsc(requestId);
        for (ItemRequest itemRequest: itemRequests ) {
            List<Item> items = itemRepository.findAllByRequestId(itemRequest.getId());
            itemRequest.setItems(items);
        }
        return ItemRequestMapper.toItemRequestDtos(itemRequests).get(0);
    }

    @Override
    public void responsesAddItems(Item item, long requestId) {
        ItemRequest request = itemRequestRepository.findById(requestId).get();
        List<Item> responses = request.getItems();
        responses.add(item);
        request.setItems(responses);
    }

    @Override
    public ItemRequest findById(long itemRequestId) throws NoSuchElementException {
        return itemRequestRepository.findById(itemRequestId).get();
    }
}

