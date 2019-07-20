package techcourse.myblog.users;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    private UserDto.Register register;
    private Long id;

    @BeforeEach
    void setUp() {
        register = new UserDto.Register("asdg@asd", "name", "P@ssw0rd", "P@ssw0rd");
        id = userService.save(register);
    }

    @Test
    void login() {
        UserDto.Response login = userService.login(register);

        assertEquals(register.getEmail(), login.getEmail());
        assertEquals(register.getName(), login.getName());
        assertNotNull(login.getId());
    }

    @Test
    void findAllExceptPassword() {
        List<UserDto.Response> users = userService.findAllExceptPassword();
        assertThat(users).isNotEmpty();
    }

    @Test
    void findById() {
        final UserDto.Response expected = userService.findById(id);

        assertEquals(register.getName(), expected.getName());
    }

    @Test
    void update() {
        UserDto.Update updateDto = new UserDto.Update("newName");
        UserDto.Response expected = userService.update(id, updateDto);

        assertEquals(expected, expected);
    }

    @Test
    void deleteById() {
        assertDoesNotThrow(() -> userService.deleteById(id));
    }

    @AfterEach
    void tearDown() {
        try {
            userService.deleteById(id);
        }catch (Exception ex){

        }
    }
}