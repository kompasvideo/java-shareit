package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.Status;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OwnerItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.ItemRequestService;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ItemServiceImplTest {
    @Mock
    ItemRepository mockItemRepository;
    @Mock
    UserRepository mockUserRepository;
    @Mock
    BookingRepository mockBookingRepository;
    @Mock
    CommentRepository mockCommentRepository;
    @Mock
    ItemRequestService mockItemRequestService;

    @Test
    void saveItem_ItemCreate() {
        Long userId = 1L;
        Item item = new Item();
        item.setId(0L);
        item.setName("Дрель");
        item.setDescription("Простая дрель");
        item.setAvailable(true);
        ItemRequest request = new ItemRequest();
        item.setRequest(request);
        User userReturn = new User();
        userReturn.setId(1L);
        userReturn.setName("updateName");
        userReturn.setEmail("updateName@user.com");
        Optional<User> optionalUserReturn = Optional.of(userReturn);
        Mockito
            .when(mockUserRepository.findById(userId))
            .thenReturn(optionalUserReturn);
        ItemDto itemDto = new ItemDto(0L, item.getName(), item.getDescription(), item.getAvailable(),
            0);
        Mockito
            .doAnswer(i -> null)
            .when(mockItemRequestService).responsesAddItems(item, itemDto.getRequestId());
        Item itemS = new Item();
        itemS.setId(1L);
        itemS.setName("Дрель");
        itemS.setDescription("Простая дрель");
        itemS.setAvailable(true);
        itemS.setOwner(userReturn);
        Mockito
            .when(mockItemRepository.save(item))
            .thenReturn(itemS);
        ModelMapper modelMapper = new ModelMapper();
        ItemService itemService = new ItemServiceImpl(mockItemRepository, mockUserRepository,
            mockBookingRepository, mockCommentRepository, modelMapper, mockItemRequestService);
        ItemDto itemDtoResult = new ItemDto(1L, itemS.getName(), itemS.getDescription(), itemS.getAvailable(),
            0);
        ItemDto itemDtoTest = itemService.saveItem(userId, itemDto);
        assertEquals(itemDtoResult.getId(), itemDtoTest.getId());
        assertEquals(itemDtoResult.getName(), itemDtoTest.getName());
        assertEquals(itemDtoResult.getDescription(), itemDtoTest.getDescription());
    }

    @Test
    void updateItem_ItemUpdate() {
        Long userId = 1L;
        Item item = new Item();
        item.setId(1L);
        item.setName("Дрель+");
        item.setDescription("Аккумуляторная дрель");
        item.setAvailable(false);
        ItemDto itemDto = new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
            0);
        User userReturn = new User();
        userReturn.setId(1L);
        userReturn.setName("updateName");
        userReturn.setEmail("updateName@user.com");
        Optional<User> optionalUserReturn = Optional.of(userReturn);
        Mockito
            .when(mockUserRepository.findById(userId))
            .thenReturn(optionalUserReturn);
        Item itemS = new Item();
        itemS.setId(1L);
        itemS.setName("Дрель");
        itemS.setDescription("Простая дрель");
        itemS.setAvailable(true);
        itemS.setOwner(userReturn);
        List<Item> items = new ArrayList<>();
        items.add(itemS);
        Mockito
            .when(mockItemRepository.findItemsByOwnerId(userId))
            .thenReturn(items);
        Optional<Item> optionalItem = Optional.of(itemS);
        Mockito
            .when(mockItemRepository.findById(userId))
            .thenReturn(optionalItem);
        Mockito
            .when(mockItemRepository.save(item))
            .thenReturn(itemS);
        ModelMapper modelMapper = new ModelMapper();
        ItemService itemService = new ItemServiceImpl(mockItemRepository, mockUserRepository,
            mockBookingRepository, mockCommentRepository, modelMapper, mockItemRequestService);
        Long itemId = 1L;
        ItemDto itemDtoTest = itemService.updateItem(userId, itemId, itemDto);
        ItemDto itemDtoResult = new ItemDto(1L, item.getName(), item.getDescription(), item.getAvailable(),
            0);
        assertEquals(itemDtoResult.getId(), itemDtoTest.getId());
        assertEquals(itemDtoResult.getName(), itemDtoTest.getName());
        assertEquals(itemDtoResult.getDescription(), itemDtoTest.getDescription());
    }

    @Test
    void getItem_ItemGet() {
        Long userId = 1L;
        Long itemId = 1L;
        User userReturn = new User();
        userReturn.setId(1L);
        userReturn.setName("updateName");
        userReturn.setEmail("updateName@user.com");
        Optional<User> optionalUserReturn = Optional.of(userReturn);
        Mockito
            .when(mockUserRepository.findById(userId))
            .thenReturn(optionalUserReturn);
        Item item = new Item();
        item.setId(1L);
        item.setName("Аккумуляторная дрель");
        item.setDescription("Аккумуляторная дрель + аккумулятор");
        item.setAvailable(true);
        item.setOwner(userReturn);
        Optional<Item> optionalItem = Optional.of(item);
        Mockito
            .when(mockItemRepository.findById(userId))
            .thenReturn(optionalItem);
        List<Comment> comments = new ArrayList<>();
        Mockito
            .when(mockCommentRepository.findAllByItemId(itemId))
            .thenReturn(comments);
        List<Booking> bookings = new ArrayList<>();
        Mockito
            .when(mockBookingRepository.findTwoBookingByOwnerIdOrderByEndAsc(userId, item.getId()))
            .thenReturn(bookings);
        ModelMapper modelMapper = new ModelMapper();
        ItemService itemService = new ItemServiceImpl(mockItemRepository, mockUserRepository,
            mockBookingRepository, mockCommentRepository, modelMapper, mockItemRequestService);
        OwnerItemDto itemDtoTest = itemService.getItem(userId, itemId);
        ItemDto itemDtoResult = new ItemDto(1L, item.getName(), item.getDescription(), item.getAvailable(),
            0);
        assertEquals(itemDtoResult.getId(), itemDtoTest.getId());
        assertEquals(itemDtoResult.getName(), itemDtoTest.getName());
        assertEquals(itemDtoResult.getDescription(), itemDtoTest.getDescription());
    }

    @Test
    void getAllItem_ItemGetAll() {
        Long userId = 1L;
        User userReturn = new User();
        userReturn.setId(1L);
        userReturn.setName("updateName");
        userReturn.setEmail("updateName@user.com");
        Optional<User> optionalUserReturn = Optional.of(userReturn);
        Mockito
            .when(mockUserRepository.findById(userId))
            .thenReturn(optionalUserReturn);
        Item item = new Item();
        item.setId(1L);
        item.setName("Аккумуляторная дрель");
        item.setDescription("Аккумуляторная дрель + аккумулятор");
        item.setAvailable(true);
        item.setOwner(userReturn);
        List<Item> items = new ArrayList<>();
        items.add(item);
        Mockito
            .when(mockItemRepository.findAllByOwnerId(userId))
            .thenReturn(items);
        ModelMapper modelMapper = new ModelMapper();
        ItemService itemService = new ItemServiceImpl(mockItemRepository, mockUserRepository,
            mockBookingRepository, mockCommentRepository, modelMapper, mockItemRequestService);
        List<OwnerItemDto> itemDtoTest = itemService.getAllItem(userId);
        ItemDto itemDtoResult = new ItemDto(1L, item.getName(), item.getDescription(), item.getAvailable(),
            0);
        assertEquals(itemDtoResult.getId(), itemDtoTest.get(0).getId());
        assertEquals(itemDtoResult.getName(), itemDtoTest.get(0).getName());
        assertEquals(itemDtoResult.getDescription(), itemDtoTest.get(0).getDescription());
    }

    @Test
    void searchItem_ItemSearchAkkumul() {
        Long userId = 1L;
        String text = "аккУМУляторная";
        User userReturn = new User();
        userReturn.setId(1L);
        userReturn.setName("updateName");
        userReturn.setEmail("updateName@user.com");
        Optional<User> optionalUserReturn = Optional.of(userReturn);
        Mockito
            .when(mockUserRepository.findById(userId))
            .thenReturn(optionalUserReturn);
        Item item = new Item();
        item.setId(1L);
        item.setName("Аккумуляторная дрель");
        item.setDescription("Аккумуляторная дрель + аккумулятор");
        item.setAvailable(true);
        item.setOwner(userReturn);
        List<Item> items = new ArrayList<>();
        items.add(item);
        Mockito
            .when(mockItemRepository.findItemsByText(text))
            .thenReturn(items);
        ModelMapper modelMapper = new ModelMapper();
        ItemService itemService = new ItemServiceImpl(mockItemRepository, mockUserRepository,
            mockBookingRepository, mockCommentRepository, modelMapper, mockItemRequestService);
        List<ItemDto> itemDtoTest = itemService.searchItem(userId, text);
        ItemDto itemDtoResult = new ItemDto(1L, item.getName(), item.getDescription(), item.getAvailable(),
            0);
        assertEquals(itemDtoResult.getId(), itemDtoTest.get(0).getId());
        assertEquals(itemDtoResult.getName(), itemDtoTest.get(0).getName());
        assertEquals(itemDtoResult.getDescription(), itemDtoTest.get(0).getDescription());
    }

    @Test
    void addComment2() {
        Long userId = 1L;
        Long itemId = 2L;
        Comment comment = new Comment();
        comment.setText("Add comment from user1");
        User user = new User();
        user.setId(4L);
        user.setName("user");
        user.setEmail("user@user.com");
        Optional<User> optionalUserReturn = Optional.of(user);
        Item item = new Item();
        item.setId(2L);
        item.setName("Отвертка");
        item.setDescription("Аккумуляторная отвертка");
        item.setAvailable(true);
        item.setOwner(user);
        Mockito
            .when(mockUserRepository.findById(userId))
            .thenReturn(optionalUserReturn);
        Optional<Item> optionalItem = Optional.of(item);
        Mockito
            .when(mockItemRepository.findById(itemId))
            .thenReturn(optionalItem);
        User user2 = new User();
        user2.setId(1L);
        user2.setName("updateName");
        user2.setEmail("updateName@user.com");
        List<Booking> bookings = new ArrayList<>();
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now());
        booking.setItem(item);
        booking.setBooker(user2);
        booking.setStatus(Status.APPROVED);
        bookings.add(booking);
        booking = new Booking();
        booking.setId(2L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now());
        booking.setItem(item);
        booking.setBooker(user2);
        booking.setStatus(Status.APPROVED);
        bookings.add(booking);
        Mockito
            .when(mockBookingRepository.findByBookerIdAndItemId(userId, itemId))
            .thenReturn(bookings);
        Mockito
            .when(mockCommentRepository.save(comment))
            .thenReturn(comment);
        ModelMapper modelMapper = new ModelMapper();
        ItemService itemService = new ItemServiceImpl(mockItemRepository, mockUserRepository,
            mockBookingRepository, mockCommentRepository, modelMapper, mockItemRequestService);
        CommentDto commentDtoTest = itemService.addComment(userId, itemId, comment);
        CommentDto commentDtoResult = new CommentDto();
        commentDtoResult.setText("Add comment from user1");
        commentDtoResult.setAuthorName("user");
        commentDtoResult.setCreated(LocalDateTime.now());
        assertEquals(commentDtoResult.getText(), commentDtoTest.getText());
        assertEquals(commentDtoResult.getAuthorName(), commentDtoTest.getAuthorName());
    }

    @Test
    void updateItem_ItemUpdateWithOtherUser() {
        Long userId = 4L;
        Item item = new Item();
        item.setId(1L);
        item.setName("Дрель+");
        item.setDescription("Аккумуляторная дрель");
        item.setAvailable(false);
        ItemDto itemDto = new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
            0);
        User userReturn = new User();
        userReturn.setId(1L);
        userReturn.setName("updateName");
        userReturn.setEmail("updateName@user.com");
        Optional<User> optionalUserReturn = Optional.of(userReturn);
        Mockito
            .when(mockUserRepository.findById(userId))
            .thenReturn(optionalUserReturn);
        Item itemS = new Item();
        itemS.setId(1L);
        itemS.setName("Дрель");
        itemS.setDescription("Простая дрель");
        itemS.setAvailable(true);
        itemS.setOwner(userReturn);
        List<Item> items = new ArrayList<>();
        Mockito
            .when(mockItemRepository.findItemsByOwnerId(userId))
            .thenReturn(items);
        Optional<Item> optionalItem = Optional.of(itemS);
        Mockito
            .when(mockItemRepository.findById(userId))
            .thenReturn(optionalItem);
        Mockito
            .when(mockItemRepository.save(item))
            .thenReturn(itemS);
        ModelMapper modelMapper = new ModelMapper();
        ItemService itemService = new ItemServiceImpl(mockItemRepository, mockUserRepository,
            mockBookingRepository, mockCommentRepository, modelMapper, mockItemRequestService);
        Long itemId = 1L;
        assertThrows(ForbiddenException.class, () -> itemService.updateItem(userId, itemId, itemDto));
    }

    @Test
    void saveItem_ItemCreateFailedByWrongUserId() {
        Long userId = 10L;
        Item item = new Item();
        item.setId(0L);
        item.setName("Дрель");
        item.setDescription("Простая дрель");
        item.setAvailable(true);
        ItemRequest request = new ItemRequest();
        item.setRequest(request);
        User userReturn = new User();
        userReturn.setId(1L);
        userReturn.setName("updateName");
        userReturn.setEmail("updateName@user.com");
        Optional<User> optionalUserReturn = Optional.empty();
        Mockito
            .when(mockUserRepository.findById(userId))
            .thenReturn(optionalUserReturn);
        ItemDto itemDto = new ItemDto(0L, item.getName(), item.getDescription(), item.getAvailable(),
            0);
        Mockito
            .doAnswer(i -> null)
            .when(mockItemRequestService).responsesAddItems(item, itemDto.getRequestId());
        Item itemS = new Item();
        itemS.setId(1L);
        itemS.setName("Дрель");
        itemS.setDescription("Простая дрель");
        itemS.setAvailable(true);
        itemS.setOwner(userReturn);
        Mockito
            .when(mockItemRepository.save(item))
            .thenReturn(itemS);
        ModelMapper modelMapper = new ModelMapper();
        ItemService itemService = new ItemServiceImpl(mockItemRepository, mockUserRepository,
            mockBookingRepository, mockCommentRepository, modelMapper, mockItemRequestService);
        ItemDto itemDtoResult = new ItemDto(1L, itemS.getName(), itemS.getDescription(), itemS.getAvailable(),
            0);
        assertThrows(NotFoundException.class, () -> itemService.saveItem(userId, itemDto));
    }
}