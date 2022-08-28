package ru.practicum.shareit.requests;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRequestServiceImplTest {

    @Mock
    ItemRequestRepository mockItemRequestRepository;
    @Mock
    UserRepository mockUserRepository;
    @Mock
    ItemRepository mockItemRepository;

    @Test
    void addNewRequest() throws Throwable {
        Long userId = 1L;
        ItemRequestInputDto itemRequestInputDto = new ItemRequestInputDto();
        itemRequestInputDto.setDescription("Хотел бы воспользоваться щёткой для обуви");
        User user = new User();
        user.setId(1L);
        user.setName("updateName");
        user.setEmail("updateName@user.com");
        Optional<User> optionalUserReturn = Optional.of(user);
        Mockito
            .when(mockUserRepository.findById(userId))
            .thenReturn(optionalUserReturn);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(null);
        itemRequest.setDescription(itemRequestInputDto.getDescription());
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setItems(new ArrayList<>());
        ItemRequest itemRequestReturn = new ItemRequest();
        itemRequestReturn.setId(1L);
        itemRequestReturn.setDescription(itemRequestInputDto.getDescription());
        itemRequestReturn.setRequester(user);
        itemRequestReturn.setCreated(LocalDateTime.now());
        itemRequestReturn.setItems(new ArrayList<>());
        Mockito
            .when(mockItemRequestRepository.save(itemRequest))
            .thenReturn(itemRequestReturn);
        ModelMapper modelMapper = new ModelMapper();
        ItemRequestService itemRequestService = new ItemRequestServiceImpl(mockItemRequestRepository, mockUserRepository,
            mockItemRepository, modelMapper);
        ItemRequestDto itemRequestDto = itemRequestService.addNewRequest(userId, itemRequestInputDto);
        assertEquals(itemRequestDto.getDescription(), "Хотел бы воспользоваться щёткой для обуви");
        assertNotNull(itemRequestDto.getCreated());
    }

    @Test
    void getListRequest() throws Throwable {
        Long userId = 1L;
        User user = new User();
        user.setId(1L);
        user.setName("updateName");
        user.setEmail("updateName@user.com");
        Optional<User> optionalUserReturn = Optional.of(user);
        Mockito
            .when(mockUserRepository.findById(userId))
            .thenReturn(optionalUserReturn);
        List<ItemRequest> itemRequests = new ArrayList<>();
        Mockito
            .when(mockItemRequestRepository.findAllByRequesterIdOrderByIdAsc(user.getId()))
            .thenReturn(itemRequests);
        ModelMapper modelMapper = new ModelMapper();
        ItemRequestService itemRequestService = new ItemRequestServiceImpl(mockItemRequestRepository, mockUserRepository,
            mockItemRepository, modelMapper);
        List<ItemRequestDto> itemRequestDtos = itemRequestService.getListRequest(userId);
        assertEquals(itemRequestDtos.size(), 0);
    }

    @Test
    void getListRequestAllUsers() throws Throwable {
        long userId = 1;
        int from = 0;
        int size = 20;
        User user = new User();
        user.setId(1L);
        user.setName("updateName");
        user.setEmail("updateName@user.com");
        User user2 = new User();
        user2.setId(4L);
        user2.setName("user");
        user2.setEmail("user@user.com");
        Item item = new Item();
        item.setId(4L);
        item.setName("Щётка для обуви");
        item.setDescription("Стандартная щётка для обуви");
        item.setAvailable(true);
        item.setOwner(user2);
        List<Item> items = new ArrayList<>();
        items.add(item);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Хотел бы воспользоваться щёткой для обуви");
        itemRequest.setRequester(user);
        LocalDateTime localDateTime = LocalDateTime.now();
        itemRequest.setCreated(localDateTime);
        itemRequest.setItems(items);
        item.setRequest(itemRequest);
        List<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.add(itemRequest);
        PageRequest pageRequest = PageRequest.of(from, size, Sort.by("created"));
        Pageable pageable = pageRequest;
        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), itemRequests.size());
        Page<ItemRequest> pItemRequests = new PageImpl<>(itemRequests.subList(start, end), pageable, itemRequests.size());
        Mockito
            .when(mockItemRequestRepository.findByIdIsNot(userId, pageRequest))
            .thenReturn(pItemRequests);
        ModelMapper modelMapper = new ModelMapper();
        ItemRequestService itemRequestService = new ItemRequestServiceImpl(mockItemRequestRepository, mockUserRepository,
            mockItemRepository, modelMapper);
        List<ItemRequestDto> itemRequestDtos = itemRequestService.getListRequestAllUsers(userId, from, size);
        assertEquals(itemRequestDtos.size(), 1);
        assertEquals(itemRequestDtos.get(0).getId(), 1);
        assertEquals(itemRequestDtos.get(0).getDescription(), "Хотел бы воспользоваться щёткой для обуви");
        assertEquals(itemRequestDtos.get(0).getCreated(), localDateTime);
    }

    @Test
    void getOneRequest() throws Throwable {
        Long userId = 1L;
        Long requestId = 1L;
        User user2 = new User();
        user2.setId(4L);
        user2.setName("user");
        user2.setEmail("user@user.com");
        User user = new User();
        user.setId(1L);
        user.setName("updateName");
        user.setEmail("updateName@user.com");
        Optional<User> optionalUserReturn = Optional.of(user);
        Mockito
            .when(mockUserRepository.findById(userId))
            .thenReturn(optionalUserReturn);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Хотел бы воспользоваться щёткой для обуви");
        itemRequest.setRequester(user);
        LocalDateTime localDateTime = LocalDateTime.now();
        itemRequest.setCreated(localDateTime);
        Item item = new Item();
        item.setId(4L);
        item.setName("Щётка для обуви");
        item.setDescription("Стандартная щётка для обуви");
        item.setAvailable(true);
        item.setOwner(user2);
        item.setRequest(null);
        List<Item> items = new ArrayList<>();
        items.add(item);
        itemRequest.setItems(items);
        item.setRequest(itemRequest);
        List<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.add(itemRequest);
        Optional<ItemRequest> optionalItemRequest = Optional.of(itemRequest);
        Mockito
            .when(mockItemRequestRepository.findAllByRequesterIdOrderByIdAsc(requestId))
            .thenReturn(itemRequests);
        Mockito
            .when(mockItemRequestRepository.findById(requestId))
            .thenReturn(optionalItemRequest);
        Mockito
            .when(mockItemRepository.findAllByRequestId(itemRequest.getId()))
            .thenReturn(items);
        ModelMapper modelMapper = new ModelMapper();
        ItemRequestService itemRequestService = new ItemRequestServiceImpl(mockItemRequestRepository, mockUserRepository,
            mockItemRepository, modelMapper);
        ItemRequestDto itemRequestDto = itemRequestService.getOneRequest(userId, requestId);
        assertEquals(itemRequestDto.getId(), 1);
        assertEquals(itemRequestDto.getDescription(), "Хотел бы воспользоваться щёткой для обуви");
        assertEquals(itemRequestDto.getCreated(), localDateTime);
        assertEquals(itemRequestDto.getItems().get(0).getId(), 4);
        assertEquals(itemRequestDto.getItems().get(0).getName(), "Щётка для обуви");
        assertEquals(itemRequestDto.getItems().get(0).getDescription(), "Стандартная щётка для обуви");
        assertEquals(itemRequestDto.getItems().get(0).getRequestId(), 1);
    }

    @Test
    void responsesAddItems() {
        Long requestId = 1L;
        User user2 = new User();
        user2.setId(4L);
        user2.setName("user");
        user2.setEmail("user@user.com");
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Хотел бы воспользоваться щёткой для обуви");
        itemRequest.setRequester(user2);
        LocalDateTime localDateTime = LocalDateTime.now();
        itemRequest.setCreated(localDateTime);
        Item item = new Item();
        item.setId(4L);
        item.setName("Щётка для обуви");
        item.setDescription("Стандартная щётка для обуви");
        item.setAvailable(true);
        item.setOwner(user2);
        List<Item> items = new ArrayList<>();
        items.add(item);
        itemRequest.setItems(items);
        item.setRequest(itemRequest);
        List<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.add(itemRequest);
        Optional<ItemRequest> optionalItemRequest = Optional.of(itemRequest);
        Mockito
            .when(mockItemRequestRepository.findById(requestId))
            .thenReturn(optionalItemRequest);
        ModelMapper modelMapper = new ModelMapper();
        ItemRequestService itemRequestService = new ItemRequestServiceImpl(mockItemRequestRepository, mockUserRepository,
            mockItemRepository, modelMapper);
        itemRequestService.responsesAddItems(item, requestId);
        assertEquals(item.getRequest().getItems().get(0).getId(), 4);
        assertEquals(item.getRequest().getItems().get(0).getName(), "Щётка для обуви");
        assertEquals(item.getRequest().getItems().get(0).getDescription(), "Стандартная щётка для обуви");
        assertEquals(item.getRequest().getItems().get(0).getAvailable(), true);
        assertEquals(item.getRequest().getItems().get(0).getOwner().getId(), 4);
    }

    @Test
    void findById() {
        Long requestId = 1L;
        User user2 = new User();
        user2.setId(4L);
        user2.setName("user");
        user2.setEmail("user@user.com");
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Хотел бы воспользоваться щёткой для обуви");
        itemRequest.setRequester(user2);
        LocalDateTime localDateTime = LocalDateTime.now();
        itemRequest.setCreated(localDateTime);
        Item item = new Item();
        item.setId(4L);
        item.setName("Щётка для обуви");
        item.setDescription("Стандартная щётка для обуви");
        item.setAvailable(true);
        item.setOwner(user2);
        List<Item> items = new ArrayList<>();
        items.add(item);
        itemRequest.setItems(items);
        item.setRequest(itemRequest);
        List<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.add(itemRequest);
        Optional<ItemRequest> optionalItemRequest = Optional.of(itemRequest);
        Mockito
            .when(mockItemRequestRepository.findById(requestId))
            .thenReturn(optionalItemRequest);
        ModelMapper modelMapper = new ModelMapper();
        ItemRequestService itemRequestService = new ItemRequestServiceImpl(mockItemRequestRepository, mockUserRepository,
            mockItemRepository, modelMapper);
        ItemRequest itemRequestResult = itemRequestService.findById(requestId);
        assertEquals(itemRequestResult.getId(), 1);
        assertEquals(itemRequestResult.getDescription(), "Хотел бы воспользоваться щёткой для обуви");
        assertEquals(itemRequestResult.getRequester().getId(), 4);
        assertEquals(itemRequestResult.getCreated(), localDateTime);
        assertEquals(itemRequestResult.getItems().size(), 1);
    }

    @Test
    void getListRequestAllUsersWrongFrom() throws Throwable {
        long userId = 1;
        int from = 0;
        int size = 20;
        User user = new User();
        user.setId(1L);
        user.setName("updateName");
        user.setEmail("updateName@user.com");
        User user2 = new User();
        user2.setId(4L);
        user2.setName("user");
        user2.setEmail("user@user.com");
        Item item = new Item();
        item.setId(4L);
        item.setName("Щётка для обуви");
        item.setDescription("Стандартная щётка для обуви");
        item.setAvailable(true);
        item.setOwner(user2);
        List<Item> items = new ArrayList<>();
        items.add(item);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Хотел бы воспользоваться щёткой для обуви");
        itemRequest.setRequester(user);
        LocalDateTime localDateTime = LocalDateTime.now();
        itemRequest.setCreated(localDateTime);
        itemRequest.setItems(items);
        item.setRequest(itemRequest);
        List<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.add(itemRequest);
        PageRequest pageRequest = PageRequest.of(from, size, Sort.by("created"));
        Pageable pageable = pageRequest;
        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), itemRequests.size());
        Page<ItemRequest> pItemRequests = new PageImpl<>(itemRequests.subList(start, end), pageable, itemRequests.size());
        Mockito
            .when(mockItemRequestRepository.findByIdIsNot(userId, pageRequest))
            .thenReturn(pItemRequests);
        ModelMapper modelMapper = new ModelMapper();
        ItemRequestService itemRequestService = new ItemRequestServiceImpl(mockItemRequestRepository, mockUserRepository,
            mockItemRepository, modelMapper);
        assertThrows(ValidationException.class, () ->itemRequestService.getListRequestAllUsers(userId, -1, size));
    }
}