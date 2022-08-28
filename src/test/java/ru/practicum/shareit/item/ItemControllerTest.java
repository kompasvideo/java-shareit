package ru.practicum.shareit.item;

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
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OwnerItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ItemControllerTest {
    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private ItemDto itemDto;
    private OwnerItemDto ownerItemDto;

    private Item item;

    private User owner;

    @BeforeEach
    void init() {
        mvc = MockMvcBuilders
            .standaloneSetup(itemController)
            .build();

        itemDto = new ItemDto(
            1L,
            "name",
            "description",
            true,
            1L
        );

        owner = new User(
            1L,
            "username",
            "username@mail.ru"
        );

        item = new Item(
            1L,
            "name",
            "description",
            true,
            owner,
            null
        );
         ownerItemDto = new OwnerItemDto();
         ownerItemDto.setId(1L);
         ownerItemDto.setName("name");
         ownerItemDto.setDescription("description");
         ownerItemDto.setAvailable(true);
    }

    @Test
    void saveItem() throws Exception {

        when(itemService.saveItem(anyLong(), any()))
            .thenReturn(itemDto);

        mvc.perform(post("/items")
                .header("X-Sharer-User-Id", 1)
                .content(mapper.writeValueAsString(itemDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
            .andExpect(jsonPath("$.name", is(itemDto.getName())))
            .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
    }

    @Test
    void updateItem() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any()))
            .thenReturn(itemDto);

        mvc.perform(patch("/items/1")
                .header("X-Sharer-User-Id", 1)
                .content(mapper.writeValueAsString(itemDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
            .andExpect(jsonPath("$.name", is(itemDto.getName())))
            .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
    }

    @Test
    void getItemById() throws Exception {
        when(itemService.getItem(anyLong(), anyLong()))
            .thenReturn(ownerItemDto);

        mvc.perform(get("/items/1")
                .header("X-Sharer-User-Id", 1)
                .content(mapper.writeValueAsString(itemDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(ownerItemDto.getId()), Long.class))
            .andExpect(jsonPath("$.name", is(ownerItemDto.getName())))
            .andExpect(jsonPath("$.description", is(ownerItemDto.getDescription())));
    }

    @Test
    void getUsersItems() throws Exception {
        when(itemService.getAllItem(anyLong()))
            .thenReturn(List.of(ownerItemDto));

        mvc.perform(get("/items")
                .header("X-Sharer-User-Id", 1)
                .content(mapper.writeValueAsString(itemDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is(ownerItemDto.getName())));
    }

    @Test
    void searchItems() throws Exception {
        when(itemService.searchItem( anyLong(), anyString()))
            .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                .header("X-Sharer-User-Id", 1)
                .param("text", "name")
                .content(mapper.writeValueAsString(itemDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is(itemDto.getName())));
    }

    @Test
    void addComment() throws Exception {
        CommentDto commentDto = new CommentDto(1L, "", "", LocalDateTime.now());

        when(itemService.addComment(anyLong(), anyLong(), any()))
            .thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                .header("X-Sharer-User-Id", 1)
                .content(mapper.writeValueAsString(itemDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.text", is(commentDto.getText())))
            .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())));
    }
}
