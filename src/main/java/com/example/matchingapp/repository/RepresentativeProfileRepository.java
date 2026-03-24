package com.example.matchingapp.repository;

import com.example.matchingapp.model.RepresentativeProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepresentativeProfileRepository extends JpaRepository<RepresentativeProfile, Long> {
    Optional<RepresentativeProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
    List<RepresentativeProfile> findByIsActiveTrueAndUserIdNot(Long userId);
}