package com.vatech.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vatech.payment.entity.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}