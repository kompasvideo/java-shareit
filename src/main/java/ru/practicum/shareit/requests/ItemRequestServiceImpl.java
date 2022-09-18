package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.InternalServerError;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestInputDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    @Override
    public ItemRequestDto addNewRequest(long userId, ItemRequestInputDto itemRequestInputDto) {
        log.info("Сохранение заявки от - {}", userId);
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(InternalServerError::new);
        if (itemRequestInputDto.getDescription() == null || itemRequestInputDto.getDescription().isEmpty()) {
            log.info("Поле description пустое");
            throw new ValidationException("Поле description пустое");
        }
        ItemRequest itemRequest = modelMapper.map(itemRequestInputDto, ItemRequest.class);
        itemRequest.setRequester(user);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequestRepository.save(itemRequest);
        ItemRequestDto itemRequestDto = modelMapper.map(itemRequest, ItemRequestDto.class);
        List<ItemRequestDto.Item> responses = new ArrayList<>();
        for (Item item : itemRequest.getItems()) {
            responses.add(new ItemRequestDto.Item(item.getId(), item.getName(), item.getDescription(),
                item.getAvailable(), item.getRequest().getId()));
        }
        itemRequestDto.setItems(responses);
        return itemRequestDto;
    }

    @Override
    public List<ItemRequestDto> getListRequest(long userId) {
        log.info("Получение всех заявок пользователя id - {}", userId);
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(InternalServerError::new);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterIdOrderByIdAsc(user.getId());
        for (ItemRequest itemRequest : itemRequests) {
            List<Item> items = itemRepository.findAllByRequestId(itemRequest.getId());
            itemRequest.setItems(items);
        }
        return ItemRequestMapper.toItemRequestDtos(itemRequests);
    }

    @Override
    public List<ItemRequestDto> getListRequestAllUsers(long userId, int from, int size) {
        log.info("Получение всех заявок ");
        if (from == 0 && size == 0) {
            throw new ValidationException("from = 0, size = 0");
        }
        if (size == 0 || size < 0) {
            throw new InternalServerError();
        }
        if (from < 0) {
            throw new ValidationException("from < 0");
        }
        PageRequest pageRequest = PageRequest.of(from, size, Sort.by("created"));
        Page<ItemRequest> itemRequests = itemRequestRepository.findByIdIsNot(userId, pageRequest);
        List<ItemRequestDto> itemRequestDtoList = itemRequests.stream()
            .map(i -> ItemRequestMapper.toItemRequestDto(i))
            .collect(Collectors.toList());
        for (ItemRequestDto itemRequestDto : itemRequestDtoList) {
            List<Item> items = itemRepository.findAllByRequestId(itemRequestDto.getId());
            itemRequestDto.setItems(ItemRequestMapper.toItemRequestDtoItem(items));
        }
        return itemRequestDtoList;
    }

    @Override
    public ItemRequestDto getOneRequest(long userId, long requestId) {
        log.info("Получение заявки с id - {}", requestId);
        Optional<User> optionalUser = userRepository.findById(userId);
        optionalUser.orElseThrow(InternalServerError::new);
        Optional<ItemRequest> optionalItemRequest = itemRequestRepository.findById(requestId);
        optionalItemRequest.orElseThrow(NotFoundException::new);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterIdOrderByIdAsc(requestId);
        for (ItemRequest itemRequest : itemRequests) {
            List<Item> items = itemRepository.findAllByRequestId(itemRequest.getId());
            itemRequest.setItems(items);
        }
        return ItemRequestMapper.toItemRequestDtos(itemRequests).get(0);
    }

    @Override
    public void responsesAddItems(Item item, long requestId) {
        log.info("Добавление вещей к заявке с id - {}", requestId);
        Optional<ItemRequest> optionalItemRequest = itemRequestRepository.findById(requestId);
        ItemRequest request = optionalItemRequest.orElseThrow();
        List<Item> responses = request.getItems();
        responses.add(item);
        request.setItems(responses);
    }

    @Override
    public ItemRequest findById(long itemRequestId) throws NoSuchElementException {
        log.info("Поиск заявки по id - {}", itemRequestId);
        Optional<ItemRequest> optionalItemRequest = itemRequestRepository.findById(itemRequestId);
        return optionalItemRequest.orElseThrow();
    }
}

