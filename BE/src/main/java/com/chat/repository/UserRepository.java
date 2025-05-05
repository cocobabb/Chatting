package com.chat.repository;

import com.chat.entity.User;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // Optional :  null일 수 있는 객체를 감싸는 래퍼 클래스
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
