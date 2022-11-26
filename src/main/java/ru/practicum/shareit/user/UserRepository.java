package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

//import java.util.List;

//public interface UserRepository {
//    User createUser(User user);
//
//    User updateUser(User user, long userId);
//
//    void deleteUser(Long userId);
//
//    User getUser(Long userId);
//
//    List<User> getUsers();
//}

public interface UserRepository extends JpaRepository<User, Long> {
}
