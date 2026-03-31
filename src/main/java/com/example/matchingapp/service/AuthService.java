package com.example.matchingapp.service;

import com.example.matchingapp.model.GroupProfile;
import com.example.matchingapp.model.RepresentativeProfile;
import com.example.matchingapp.model.User;
import com.example.matchingapp.repository.GroupProfileRepository;
import com.example.matchingapp.repository.RepresentativeProfileRepository;
import com.example.matchingapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RepresentativeProfileRepository representativeProfileRepository;
    private final GroupProfileRepository groupProfileRepository;

    public AuthService(
            UserRepository userRepository,
            RepresentativeProfileRepository representativeProfileRepository,
            GroupProfileRepository groupProfileRepository
    ) {
        this.userRepository = userRepository;
        this.representativeProfileRepository = representativeProfileRepository;
        this.groupProfileRepository = groupProfileRepository;
    }

    public User signup(User user) {
        userRepository.findByEmail(user.getEmail()).ifPresent(existing -> {
            throw new IllegalArgumentException("このメールアドレスはすでに登録されています");
        });

        User savedUser = userRepository.save(user);

        RepresentativeProfile representativeProfile = new RepresentativeProfile();
        representativeProfile.setUserId(savedUser.getId());
        representativeProfile.setNickname(savedUser.getName());
        representativeProfile.setIsActive(true);
        representativeProfileRepository.save(representativeProfile);

        GroupProfile groupProfile = new GroupProfile();
        groupProfile.setOwnerUserId(savedUser.getId());
        groupProfile.setTitle(savedUser.getName() + "さんのグループ");
        groupProfile.setMaleCount(0);
        groupProfile.setFemaleCount(0);
        groupProfile.setStatus("ACTIVE");
        groupProfileRepository.save(groupProfile);

        return savedUser;
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