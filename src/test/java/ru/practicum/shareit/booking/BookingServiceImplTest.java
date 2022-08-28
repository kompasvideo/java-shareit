package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import ru.practicum.shareit.Status;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BookingServiceImplTest {
    @Mock
    BookingRepository mockBookingRepository;

    @Mock
    UserRepository mockUserRepository;

    @Mock
    ItemRepository mockItemRepository;

    @Test
    void save() {
        Long userId = 1L;
        Long itemId = 2L;
        BookingCreateDto bookingCreateDto = new BookingCreateDto(0L, 2L, LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusDays(1));
        User user = new User();
        user.setId(4L);
        user.setName("user");
        user.setEmail("user@user.com");
        Optional<User> optionalUserReturn = Optional.of(user);
        Mockito
            .when(mockUserRepository.findById(userId))
            .thenReturn(optionalUserReturn);
        Item item = new Item();
        item.setId(2L);
        item.setName("Отвертка");
        item.setDescription("Аккумуляторная отвертка");
        item.setAvailable(true);
        item.setOwner(user);
        Optional<Item> optionalItem = Optional.of(item);
        Mockito
            .when(mockItemRepository.findById(itemId))
            .thenReturn(optionalItem);
        ModelMapper modelMapper = new ModelMapper();
        Booking booking = new Booking();
        booking.setId(0L);
        booking.setStart(bookingCreateDto.getStart());
        booking.setEnd(bookingCreateDto.getEnd());
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Status.WAITING);
        Booking booking2 = new Booking();
        booking2.setId(1L);
        booking2.setStart(bookingCreateDto.getStart());
        booking2.setEnd(bookingCreateDto.getEnd());
        booking2.setItem(item);
        booking2.setBooker(user);
        booking2.setStatus(Status.WAITING);
        Mockito
            .when(mockBookingRepository.save(Mockito.any()))
            .thenReturn(booking2);
        BookingService bookingService = new BookingServiceImpl(mockBookingRepository, mockUserRepository,
            mockItemRepository, modelMapper);
        BookingCreateDto bookingCreateDtoReturn = bookingService.save(bookingCreateDto, userId);
        assertEquals(bookingCreateDtoReturn.getItemId(), 2);
    }

    @Test
    void update() {
        Long userId = 1L;
        Long itemId = 2L;
        Long bookingId = 1L;
        Boolean approved = true;
        BookingCreateDto bookingCreateDto = new BookingCreateDto(0L, 2L, LocalDateTime.now().plusHours(1),
            LocalDateTime.now().plusDays(1));
        User user2 = new User();
        user2.setId(1L);
        user2.setName("updateName");
        user2.setEmail("updateName@user.com");
        User user = new User();
        user.setId(4L);
        user.setName("user");
        user.setEmail("user@user.com");
        Optional<User> optionalUserReturn = Optional.of(user);
        Mockito
            .when(mockUserRepository.findById(userId))
            .thenReturn(optionalUserReturn);
        Item item = new Item();
        item.setId(2L);
        item.setName("Отвертка");
        item.setDescription("Аккумуляторная отвертка");
        item.setAvailable(true);
        item.setOwner(user2);
        Optional<Item> optionalItem = Optional.of(item);
        Mockito
            .when(mockItemRepository.findById(itemId))
            .thenReturn(optionalItem);
        ModelMapper modelMapper = new ModelMapper();
        Booking booking = new Booking();
        booking.setId(0L);
        booking.setStart(bookingCreateDto.getStart());
        booking.setEnd(bookingCreateDto.getEnd());
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Status.WAITING);
        Booking booking2 = new Booking();
        booking2.setId(1L);
        booking2.setStart(bookingCreateDto.getStart());
        booking2.setEnd(bookingCreateDto.getEnd());
        booking2.setItem(item);
        booking2.setBooker(user2);
        booking2.setStatus(Status.WAITING);
        Optional<Booking> optionalBooking2 = Optional.of(booking2);
        Mockito
            .when(mockBookingRepository.findById(Mockito.any()))
            .thenReturn(optionalBooking2);
        Mockito
            .when(mockBookingRepository.save(Mockito.any()))
            .thenReturn(booking2);
        BookingService bookingService = new BookingServiceImpl(mockBookingRepository, mockUserRepository,
            mockItemRepository, modelMapper);
        BookingDto bookingDtoReturn = bookingService.update(userId, bookingId, approved);
        assertEquals(bookingDtoReturn.getId(), 1);
        assertEquals(bookingDtoReturn.getStatus(), Status.APPROVED);
        assertEquals(bookingDtoReturn.getBooker().getId(), 1);
        assertEquals(bookingDtoReturn.getItem().getId(), 2);
        assertEquals(bookingDtoReturn.getItem().getName(), "Отвертка");
    }

    @Test
    void get() {
        Long userId = 1L;
        Long itemId = 2L;
        Long bookingId = 2L;
        User user = new User();
        user.setId(4L);
        user.setName("user");
        user.setEmail("user@user.com");
        Optional<User> optionalUserReturn = Optional.of(user);
        Mockito
            .when(mockUserRepository.findById(userId))
            .thenReturn(optionalUserReturn);
        Item item = new Item();
        item.setId(2L);
        item.setName("Отвертка");
        item.setDescription("Аккумуляторная отвертка");
        item.setAvailable(true);
        item.setOwner(user);
        User user2 = new User();
        user2.setId(1L);
        user2.setName("updateName");
        user2.setEmail("updateName@user.com");
        Booking booking = new Booking();
        booking.setId(2L);
        booking.setStart(LocalDateTime.now().plusHours(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setItem(item);
        booking.setBooker(user2);
        booking.setStatus(Status.WAITING);
        Optional<Booking> optionalBooking2 = Optional.of(booking);
        Mockito
            .when(mockBookingRepository.findById(bookingId))
            .thenReturn(optionalBooking2);
        ModelMapper modelMapper = new ModelMapper();
        BookingService bookingService = new BookingServiceImpl(mockBookingRepository, mockUserRepository,
            mockItemRepository, modelMapper);
        BookingDto bookingDtoReturn = bookingService.get(userId, bookingId);
        assertEquals(bookingDtoReturn.getId(), 2);
        assertEquals(bookingDtoReturn.getStatus(), Status.WAITING);
        assertEquals(bookingDtoReturn.getBooker().getId(), 1);
        assertEquals(bookingDtoReturn.getItem().getId(), 2);
        assertEquals(bookingDtoReturn.getItem().getName(), "Отвертка");
    }

    @Test
    void getAllByCurrentUser() {
        Long userId = 1L;
        String state = "ALL";
        User user = new User();
        user.setId(1L);
        user.setName("updateName");
        user.setEmail("updateName@user.com");
        Optional<User> optionalUserReturn = Optional.of(user);
        Mockito
            .when(mockUserRepository.findById(userId))
            .thenReturn(optionalUserReturn);
        Item item = new Item();
        item.setId(2L);
        item.setName("Отвертка");
        item.setDescription("Аккумуляторная отвертка");
        item.setAvailable(true);
        item.setOwner(user);
        User user2 = new User();
        user2.setId(1L);
        user2.setName("updateName");
        user2.setEmail("updateName@user.com");
        Booking booking = new Booking();
        booking.setId(2L);
        booking.setStart(LocalDateTime.now().plusHours(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Status.WAITING);
        List<Booking> listBookings = new ArrayList<>();
        listBookings.add(booking);
        booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().plusHours(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Status.APPROVED);
        listBookings.add(booking);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Pageable pageable = pageRequest;
        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), listBookings.size());
        Page<Booking> pBookings = new PageImpl<>(listBookings.subList(start, end), pageable, listBookings.size());
        Mockito
            .when(mockBookingRepository.findAllByBookerIdOrderByEndDesc(userId, pageRequest ))
            .thenReturn(pBookings);
        ModelMapper modelMapper = new ModelMapper();
        BookingService bookingService = new BookingServiceImpl(mockBookingRepository, mockUserRepository,
            mockItemRepository, modelMapper);
        List<BookingDto> bookingDtoReturn = bookingService.getAllByCurrentUser(userId, state, 0, 10);
        assertEquals(bookingDtoReturn.get(0).getId(), 2);
        assertEquals(bookingDtoReturn.get(0).getStatus(), Status.WAITING);
        assertEquals(bookingDtoReturn.get(0).getBooker().getId(), 1);
        assertEquals(bookingDtoReturn.get(0).getItem().getId(), 2);
        assertEquals(bookingDtoReturn.get(0).getItem().getName(), "Отвертка");
        assertEquals(bookingDtoReturn.get(1).getId(), 1);
        assertEquals(bookingDtoReturn.get(1).getStatus(), Status.APPROVED);
        assertEquals(bookingDtoReturn.get(1).getBooker().getId(), 1);
        assertEquals(bookingDtoReturn.get(1).getItem().getId(), 2);
        assertEquals(bookingDtoReturn.get(1).getItem().getName(), "Отвертка");
    }

    @Test
    void getAllByOwnedItems() {
        Long userId = 1L;
        String state = "ALL";
        User user = new User();
        user.setId(1L);
        user.setName("updateName");
        user.setEmail("updateName@user.com");
        Optional<User> optionalUserReturn = Optional.of(user);
        Mockito
            .when(mockUserRepository.findById(userId))
            .thenReturn(optionalUserReturn);
        Item item = new Item();
        item.setId(2L);
        item.setName("Отвертка");
        item.setDescription("Аккумуляторная отвертка");
        item.setAvailable(true);
        item.setOwner(user);
        User user2 = new User();
        user2.setId(1L);
        user2.setName("updateName");
        user2.setEmail("updateName@user.com");
        Booking booking = new Booking();
        booking.setId(2L);
        booking.setStart(LocalDateTime.now().plusHours(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Status.WAITING);
        List<Booking> listBookings = new ArrayList<>();
        listBookings.add(booking);
        booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().plusHours(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Status.APPROVED);
        listBookings.add(booking);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Pageable pageable = pageRequest;
        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), listBookings.size());
        Page<Booking> pBookings = new PageImpl<>(listBookings.subList(start, end), pageable, listBookings.size());
        Mockito
            .when(mockBookingRepository.findAllByOwnerIdOrderByEndDesc(userId, pageRequest))
            .thenReturn(pBookings);
        ModelMapper modelMapper = new ModelMapper();
        BookingService bookingService = new BookingServiceImpl(mockBookingRepository, mockUserRepository,
            mockItemRepository, modelMapper);
        List<BookingDto> bookingDtoReturn = bookingService.getAllByOwnedItems(userId, state, 0, 10);
        assertEquals(bookingDtoReturn.get(0).getId(), 2);
        assertEquals(bookingDtoReturn.get(0).getStatus(), Status.WAITING);
        assertEquals(bookingDtoReturn.get(0).getBooker().getId(), 1);
        assertEquals(bookingDtoReturn.get(0).getItem().getId(), 2);
        assertEquals(bookingDtoReturn.get(0).getItem().getName(), "Отвертка");
        assertEquals(bookingDtoReturn.get(1).getId(), 1);
        assertEquals(bookingDtoReturn.get(1).getStatus(), Status.APPROVED);
        assertEquals(bookingDtoReturn.get(1).getBooker().getId(), 1);
        assertEquals(bookingDtoReturn.get(1).getItem().getId(), 2);
        assertEquals(bookingDtoReturn.get(1).getItem().getName(), "Отвертка");
    }
}
