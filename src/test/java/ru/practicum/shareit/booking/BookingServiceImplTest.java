package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

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
        Long userId = 1l;
        Long itemId = 2l;
        BookingCreateDto bookingCreateDto = new BookingCreateDto(0l,2l,LocalDateTime.now(), LocalDateTime.now());
        User user2 = new User();
        user2.setId(1l);
        user2.setName("updateName");
        user2.setEmail("updateName@user.com");
        User user = new User();
        user.setId(4l);
        user.setName("user");
        user.setEmail("user@user.com");
        Optional<User> optionalUserReturn = Optional.of(user);
        Mockito
            .when(mockUserRepository.findById(userId))
            .thenReturn(optionalUserReturn);
        Item item = new Item();
        item.setId(2l);
        item.setName("Отвертка");
        item.setDescription("Аккумуляторная отвертка");
        item.setAvailable(true);
        item.setOwner(user);
        Optional<Item> optionalItem = Optional.of(item);
        Mockito
            .when(mockItemRepository.findById(itemId))
            .thenReturn(optionalItem);



        BookingRepository mockBookingRepository = Mockito.mock(BookingRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        ModelMapper modelMapper = new ModelMapper();

        BookingService bookingService = new BookingServiceImpl(mockBookingRepository, mockUserRepository,
            mockItemRepository, modelMapper);
//        Mockito
//            .when(mockUserRepository.findById(userId).get())
//            .thenReturn()

//        Booking booking = new Booking();
//        booking.setId(1l);
        //booking.setItem(2l);

//        Mockito
//            .when(modelMapper.map(bookingCreateDto, Booking.class))
//            .thenReturn();


    }

    @Test
    void update() {

    }

    @Test
    void get() {

    }

    @Test
    void getAllByCurrentUser() {

    }

    @Test
    void getAllByOwnedItems() {

    }
}
