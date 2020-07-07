package ru.iteco.training.kafkachat.service;

import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.iteco.training.kafkachat.entity.User;
import ru.iteco.training.kafkachat.enums.UserRole;
import ru.iteco.training.kafkachat.repository.UserRepository;
import ru.iteco.training.kafkachat.repository.UserRoleRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Value("${user.login}")
    private String currentUserLogin;
    private User currentUser;
    private Set<UserRole> currentUserRoles;

    @Order(1)
    @EventListener(ContextRefreshedEvent.class)
    public void initCurrentUser() {
        currentUser =  userRepository.findByLogin(currentUserLogin);
        if (currentUser == null) {
            throw new RuntimeException("User with login " + currentUserLogin + " not exists.");
        }

        currentUserRoles = userRoleRepository.findByUserId(currentUser.getId())
                .stream().map(ru.iteco.training.kafkachat.entity.UserRole::getRole)
                .collect(Collectors.toSet());
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Set<UserRole> getCurrentUserRoles() {
        return currentUserRoles;
    }

    public boolean hasRoles(Set<UserRole> roles) {
        return !Collections.disjoint(currentUserRoles, roles);
    }

    public boolean userExists(String login) {
        return userRepository.findByLogin(login) != null;
    }

    @Transactional
    public void createUser(String login, String name, UserRole... roles) {
        User user = new User();
        user.setLogin(login);
        user.setName(name);
        user.setCreationTimestamp(new Date());

        User saved = userRepository.save(user);
        for (UserRole role : roles) {
            ru.iteco.training.kafkachat.entity.UserRole roleEntity = new ru.iteco.training.kafkachat.entity.UserRole();
            roleEntity.setUserId(saved.getId());
            roleEntity.setRole(role);
            userRoleRepository.save(roleEntity);
        }
    }

    public List<String> getAllUsersLogins() {
        Iterable<User> users = userRepository.findAll();
        return StreamSupport.stream(users.spliterator(), false).map(User::getLogin).collect(Collectors.toList());
    }
}
