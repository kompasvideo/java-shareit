package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.user.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    UserRepository mockUserRepository;

    @Test
    void saveUser_UserCreate() throws Throwable {
        User user = new User();
        user.setName("user");
        user.setEmail("user@user.com");
        User userReturn = new User();
        userReturn.setId(1L);
        userReturn.setName("user");
        userReturn.setEmail("user@user.com");
        Mockito
            .when(mockUserRepository.save(user))
            .thenReturn(userReturn);
        UserService userService = new UserServiceImpl(mockUserRepository);
        User userTest = userService.saveUser(user);
        Assertions.assertEquals(userReturn.getId(),userTest.getId());
        Assertions.assertEquals(userReturn.getEmail(),userTest.getEmail());
        Assertions.assertEquals(userReturn.getName(),userTest.getName());
    }

    @Test
    void saveUser_UserCreateDuplicateEmail() {
        User user = new User();
        user.setName("user");
        user.setEmail("user@user.com");
        Mockito
            .when(mockUserRepository.save(user))
            .thenThrow(RuntimeException.class);
        UserService userService = new UserServiceImpl(mockUserRepository);
        Assertions.assertThrows(RuntimeException.class,() -> userService.saveUser(user));
    }

    @Test
    void saveUser_UserCreateFailNoEmail() {
        User user = new User();
        user.setName("user");
        UserService userService = new UserServiceImpl(mockUserRepository);
        Assertions.assertThrows(BadRequestException.class,() -> userService.saveUser(user));
    }

    @Test
    void saveUser_UserCreateFailInvalidEmail() {
        User user = new User();
        user.setName("user");
        user.setEmail("user.com");
        UserService userService = new UserServiceImpl(mockUserRepository);
        Assertions.assertThrows(BadRequestException.class,() -> userService.saveUser(user));
    }

    @Test
    void updateUser() throws Throwable {
        User user = new User();
        user.setName("update");
        user.setEmail("update@user.com");
        Long userId = 1L;
        User userReturn = new User();
        userReturn.setId(1L);
        userReturn.setName("user");
        userReturn.setEmail("user@user.com");
        User userReturnSave = new User();
        userReturnSave.setId(1L);
        userReturnSave.setName("update");
        userReturnSave.setEmail("update@user.com");
        Optional<User> optionalUserReturn = Optional.of(userReturn);
        List<User> users = new ArrayList<>();
        users.add(userReturn);
        Mockito
            .when(mockUserRepository.findById(userId))
            .thenReturn(optionalUserReturn);
        Mockito
            .when(mockUserRepository.findAll())
            .thenReturn(users);
        Mockito
            .when(mockUserRepository.save(userReturnSave))
            .thenReturn(userReturnSave);
        UserService userService = new UserServiceImpl(mockUserRepository);
        User userTest = userService.updateUser(userId, user);
        Assertions.assertEquals(userReturnSave.getId(),userTest.getId());
        Assertions.assertEquals(userReturnSave.getEmail(),userTest.getEmail());
        Assertions.assertEquals(userReturnSave.getName(),userTest.getName());
    }

    @Test
    void getAllUsers() {
        User userReturn = new User();
        userReturn.setId(1L);
        userReturn.setName("update");
        userReturn.setEmail("update@user.com");
        List<User> users = new ArrayList<>();
        users.add(userReturn);
        Mockito
            .when(mockUserRepository.findAll())
            .thenReturn(users);
        UserService userService = new UserServiceImpl(mockUserRepository);
        List<User> userTests = userService.getAllUsers();
        Assertions.assertEquals(userReturn.getId(),userTests.get(0).getId());
        Assertions.assertEquals(userReturn.getEmail(),userTests.get(0).getEmail());
        Assertions.assertEquals(userReturn.getName(),userTests.get(0).getName());
    }



    @Test
    void getUser() {
        Long userId = 1L;
        User userReturn = new User();
        userReturn.setId(1L);
        userReturn.setName("update");
        userReturn.setEmail("update@user.com");
        Optional<User> optionalUserReturn = Optional.of(userReturn);
        Mockito
            .when(mockUserRepository.findById(userId))
            .thenReturn(optionalUserReturn);
        UserService userService = new UserServiceImpl(mockUserRepository);
        User userTest = userService.getUser(userId);
        Assertions.assertEquals(userReturn.getId(),userTest.getId());
        Assertions.assertEquals(userReturn.getEmail(),userTest.getEmail());
        Assertions.assertEquals(userReturn.getName(),userTest.getName());
    }

    @Test
    void deleteUser() {
        Long userId = 3L;
        User userReturn = new User();
        userReturn.setId(1L);
        userReturn.setName("update");
        userReturn.setEmail("update@user.com");
        Optional<User> optionalUserReturn = Optional.of(userReturn);
        Mockito
            .when(mockUserRepository.findById(userId))
            .thenReturn(optionalUserReturn);
        Mockito
            .doAnswer(i -> null)
            .when(mockUserRepository).deleteById(userId);
        UserService userService = new UserServiceImpl(mockUserRepository);
        userService.deleteUser(userId);
        Mockito.verify(mockUserRepository, Mockito.times(1)).deleteById(userId);
    }
}