package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.Status;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BookingControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private final List<BookingDto> result = new ArrayList<>();
    @Mock
    private BookingService bookingService;
    @InjectMocks
    private BookingController bookingController;
    private BookingCreateDto bookingCreateDto;
    private BookingDto bookingDto;
    private Booking booking;
    private MockMvc mvc;

    @BeforeEach
    void init() {
        mvc = MockMvcBuilders
            .standaloneSetup(bookingController)
            .build();

        bookingCreateDto = new BookingCreateDto(
            1L,
            1L,
            null,
            null
        );
        Item item = new Item(
            1L,
            "name",
            "description",
            true,
            new User(1L, "Alex", "email@mail.ru"),
            null
        );

        User booker = new User(2L, "booker", "booker@yandex.ru");

        booking = new Booking(
            1L,
            null,
            null,
            item,
            booker,
            Status.APPROVED);
        bookingDto = new BookingDto(
            1L,
            null,
            null,
            item,
            booker,
            Status.APPROVED
        );
        BookingDto bookingDto2 = new BookingDto(
            1L,
            null,
            null,
            null,
            null,
            Status.APPROVED
        );
        result.add(bookingDto);
    }

    @Test
    void save() throws Exception {
        when(bookingService.save(any(), anyLong()))
            .thenReturn(bookingCreateDto);

        mvc.perform(post("/bookings")
                .header("X-Sharer-User-Id", 1)
                .content(mapper.writeValueAsString(bookingCreateDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(bookingCreateDto.getId()), Long.class))
            .andExpect(jsonPath("$.id", is(bookingCreateDto.getItemId()), Long.class));
    }

    @Test
    void update() throws Exception {
        when(bookingService.update(anyLong(), anyLong(), anyBoolean()))
            .thenReturn(bookingDto);

        mvc.perform(patch("/bookings/1")
                .header("X-Sharer-User-Id", 1)
                .param("approved", "true")
                .content(mapper.writeValueAsString(bookingCreateDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
            .andExpect(jsonPath("$.status", is(Status.APPROVED.name())))
            .andExpect(jsonPath("$.booker.id", is(booking.getBooker().getId()), Long.class))
            .andExpect(jsonPath("$.item.id", is(booking.getItem().getId()), Long.class));
    }

    @Test
    void get() throws Exception {
        when(bookingService.get(anyLong(), anyLong()))
            .thenReturn(bookingDto);

        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/bookings/1")
                .header("X-Sharer-User-Id", 1)
                .content(mapper.writeValueAsString(booking))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
            .andExpect(jsonPath("$.booker.id", is(booking.getBooker().getId()), Long.class))
            .andExpect(jsonPath("$.item.id", is(booking.getItem().getId()), Long.class));
    }

    @Test
    void getAllByOwnedItems() throws Exception {
        when(bookingService.getAllByOwnedItems(anyLong(), anyString(), anyInt(), anyInt()))
            .thenReturn(result);

        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/bookings/owner")
                .header("X-Sharer-User-Id", 1)
                .param("state", "All")
                .param("from", "0")
                .param("size", "10")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
            .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId()), Long.class))
            .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId()), Long.class));
    }

    @Test
    void getAllByCurrentUser() throws Exception {
        when(bookingService.getAllByCurrentUser(anyLong(), anyString(), anyInt(), anyInt()))
            .thenReturn(result);

        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/bookings")
                .header("X-Sharer-User-Id", 1)
                .param("state", "All")
                .param("from", "0")
                .param("size", "5")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
            .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId()), Long.class))
            .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId()), Long.class));
    }
}
