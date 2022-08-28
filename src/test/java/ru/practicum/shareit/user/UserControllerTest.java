package ru.practicum.shareit.user;

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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;
import java.nio.charset.StandardCharsets;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserControllerTest {
    @Mock
    private UserService mockUserService;

    @InjectMocks
    private UserController mockUserController;

    private MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private User user;

    @BeforeEach
    void init() {
        mvc = MockMvcBuilders
            .standaloneSetup(mockUserController)
            .build();

        user = new User();
        user.setId(1L);
        user.setName("updateName");
        user.setEmail("updateName@user.com");
    }

    @Test
    void saveNewUser() throws Throwable {
        when(mockUserService.saveUser(any()))
            .thenReturn(user);

        mvc.perform(post("/users")
                .content(mapper.writeValueAsString(user))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is(user.getName())))
            .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void updateUser() throws Throwable {
        when(mockUserService.updateUser(anyLong(), any()))
            .thenReturn(user);
        mvc.perform(patch("/users/1")
                .content(mapper.writeValueAsString(user))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is(user.getName())))
            .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void getAllUsers() throws Exception {
        List<User> users = List.of(user);

        when(mockUserService.getAllUsers())
            .thenReturn(users);

        mvc.perform(get("/users")
                .content(mapper.writeValueAsString(user))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getUser() throws Exception {
        when(mockUserService.getUser(anyLong()))
            .thenReturn(user);

        mvc.perform(get("/users/1")
                .content(mapper.writeValueAsString(user))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is(user.getName())))
            .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void updateUserWithException() throws Throwable {
        when(mockUserService.updateUser(anyLong(), any()))
            .thenThrow(NotFoundException.class);
        mvc.perform(patch("/users/1")
                .content(mapper.writeValueAsString(null))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is(400));
    }
}
