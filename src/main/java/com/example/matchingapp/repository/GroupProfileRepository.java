package com.example.matchingapp.repository;

import com.example.matchingapp.model.GroupProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupProfileRepository extends JpaRepository<GroupProfile, Long> {
    Optional<GroupProfile> findByOwnerUserId(Long ownerUserId);
}
