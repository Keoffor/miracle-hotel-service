package com.miracle.keoffor.repository;

import com.miracle.keoffor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    void deleteByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
    Optional<User> findByIdAndEmail(Long customerId, String email);
}
