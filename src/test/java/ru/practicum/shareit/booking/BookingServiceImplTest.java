package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

@SpringBootTest
public class BookingServiceImplTest {




    @Test
    void testSave() {
        BookingCreateDto bookingCreateDto = new BookingCreateDto(0l,2l,LocalDateTime.now(), LocalDateTime.now());
        Long userId = 1l;
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

}
