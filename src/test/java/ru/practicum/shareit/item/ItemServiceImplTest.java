package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemServiceImplTest {

    @Mock
    UserRepository mockUserRepository;

    @Mock
    ItemRepository mockItemRepository;

    @Test
    void saveItem() {
    }

    @Test
    void updateItem() {
    }

    @Test
    void getItem() {
    }

    @Test
    void getAllItem() {
    }

    @Test
    void searchItem() {
    }

    @Test
    void addComment() {
    }
}