package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    User findByEmail(String email);
    List<User> allUsers();
    void saveUser(User user);
    User getUser(Long id);
    void updateUser(User user);
    void deleteUser(Long id);
    void setUserRoles(User user, Set<Long> roleIds);
}
