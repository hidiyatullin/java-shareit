package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.Exeption.IncorrectEmailException;
import ru.practicum.shareit.Exeption.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserRepository {

    HashMap<Long, User> users = new HashMap<>();
    Set<String> setEmail = new HashSet<>();
    private long userId = 0;

    public User createUser(User user) {
        if(!setEmail.contains(user.getEmail())) {
            setEmail.add(user.getEmail());
            userId++;
            user.setId(userId);
            users.put(userId, user);
            return user;
        } else {
            throw new IncorrectEmailException("Пользователь с таким email уже есть в базе");
        }

    }

    public User updateUser(User user, long userId) {
        if (!setEmail.contains(user.getEmail())) {
            setEmail.remove(users.get(userId).getEmail());
            setEmail.add(user.getEmail());
            User oldUser = users.get(userId);
            if (user.getEmail() != null) {
                oldUser.setEmail(user.getEmail());
            }
            if (user.getName() != null) {
                oldUser.setName(user.getName());
            }
//        users.put(userId, user);
            return oldUser;
        } throw new IncorrectEmailException("Пользователь с таким email уже есть в базе");
    }


    public void deleteUser(Long userId) {
        setEmail.remove(users.get(userId).getEmail());
        users.remove(userId);
    }

    public User getUser(Long userId) {
        if (users.containsKey(userId)) {
            return users.get(userId);
        }
        throw new UserNotFoundException("Не найден пользователь с id " + userId);
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }
}
