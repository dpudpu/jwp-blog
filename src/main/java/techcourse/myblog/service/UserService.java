package techcourse.myblog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.ValidUserException;
import techcourse.myblog.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    public static final String EMAIL_DUPLICATE_MESSAGE = "이미 사용중인 이메일입니다.";
    public static final String PASSWORD_INVALID_MESSAGE = "비밀번호와 비밀번호 확인이 일치하지 않습니다.";
    public static final String EMAIL_OR_PASSWORD_NOT_MATCH = "존재하지 않는 이메일 또는 비밀번호가 틀립니다.";

    private final UserRepository userRepository;

    public Long save(UserDto.Register userDto) {
        verifyDuplicateEmail(userDto.getEmail());
        verifyPassword(userDto);

        User user = userDto.toUser();

        return userRepository.save(user).getId();
    }

    private void verifyPassword(UserDto.Register userDto) {
        if (!userDto.isValidPassword()) {
            throw new ValidUserException(PASSWORD_INVALID_MESSAGE, "password");
        }
    }

    private void verifyDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ValidUserException(EMAIL_DUPLICATE_MESSAGE, "email");
        }
    }

    public User login(UserDto.Register userDto) {
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new ValidUserException(EMAIL_OR_PASSWORD_NOT_MATCH, "password"));

        if (!user.authenticate(userDto.getPassword())) {
            throw new ValidUserException(EMAIL_OR_PASSWORD_NOT_MATCH, "password");
        }

        return user;
    }

    public List<UserDto.Response> findAllExceptPassword() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserDto.Response::createByUser)
                .collect(Collectors.toList());
    }

    public UserDto.Response findById(Long id) {
        User user = getUserById(id);

        return UserDto.Response.createByUser(user);
    }

    public UserDto.Response update(UserDto.Update userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. : " + userDto.getId()));

        user.update(userDto.toUser());

        return UserDto.Response.createByUser(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User getUserById(final Long id ){
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. : " + id));
    }

    public User findByEmail(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 email 입니다."));
    }
}

