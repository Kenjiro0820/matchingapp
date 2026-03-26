package com.example.matchingapp.service;

import com.example.matchingapp.model.User;
import com.example.matchingapp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User signup(User user) {
        userRepository.findByEmail(user.getEmail()).ifPresent(existing -> {
            throw new IllegalArgumentException("このメールアドレスはすでに登録されています");
        });

        return userRepository.save(user);
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("パスワードが違います");
        }

        return user;
    }
}
