package ru.practicum.shareit.requests;

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
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ItemRequestControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private ItemRequestService itemRequestService;
    @InjectMocks
    private ItemRequestController controller;
    private ItemRequestDto itemRequestDto;
    private MockMvc mvc;

    @BeforeEach
    void init() {
        mvc = MockMvcBuilders
            .standaloneSetup(controller)
            .build();

        itemRequestDto = new ItemRequestDto(
            1L,
            "description",
            null,
            new ArrayList<>()
        );
    }

    @Test
    void addNewRequest() throws Throwable {
        when(itemRequestService.addNewRequest(anyLong(), any()))
            .thenReturn(itemRequestDto);

        mvc.perform(post("/requests")
                .header("X-Sharer-User-Id", 1)
                .content(mapper.writeValueAsString(itemRequestDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
            .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    void getOneRequest() throws Throwable {
        when(itemRequestService.getOneRequest(anyLong(), anyLong()))
            .thenReturn(itemRequestDto);

        mvc.perform(get("/requests/1")
                .header("X-Sharer-User-Id", 1)
                .content(mapper.writeValueAsString(itemRequestDto.toString()))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
            .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    void getListRequest() throws Throwable {
        List<ItemRequestDto> requests = List.of(itemRequestDto);

        when(itemRequestService.getListRequest(anyLong()))
            .thenReturn(requests);

        mvc.perform(get("/requests")
                .header("X-Sharer-User-Id", 1)
                .content(mapper.writeValueAsString(itemRequestDto.toString()))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(itemRequestDto.getId()), Long.class))
            .andExpect(jsonPath("$[0].items", hasSize(0)));
    }

    @Test
    void getListRequestAllUsers() throws Throwable {
        List<ItemRequestDto> requests = List.of(itemRequestDto);

        when(itemRequestService.getListRequestAllUsers(anyLong(), anyInt(), anyInt()))
            .thenReturn(requests);

        mvc.perform(get("/requests/all")
                .header("X-Sharer-User-Id", 1)
                .param("from", "0")
                .param("size", "10")
                .content(mapper.writeValueAsString(itemRequestDto.toString()))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(itemRequestDto.getId()), Long.class))
            .andExpect(jsonPath("$[0].items", hasSize(0)));
    }
}

