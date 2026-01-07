package com.spring.JournalApplication.service;

import com.spring.JournalApplication.entity.User;
import com.spring.JournalApplication.repository.UserRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void saveUser(User user) {
        userRepository.save(user);
        log.info("User details updated.");
    }

    public void saveUser(User user, boolean encodePassword) {
        if (encodePassword) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setRoles(Arrays.asList("USER"));
        userRepository.save(user);
        log.info("User {} saved", user.getUsername());
    }

    public void saveAdmin(User user, boolean encodePassword) {
        if (encodePassword) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setRoles(Arrays.asList("USER", "ADMIN"));
        userRepository.save(user);
        log.info("New Admin {} saved", user.getUsername());
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getUserById(ObjectId id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUserById(ObjectId id) { userRepository.deleteById(id); }

    public User findByUsername(@NonNull String username) {
        return userRepository.findByUsername(username);
    }
}
