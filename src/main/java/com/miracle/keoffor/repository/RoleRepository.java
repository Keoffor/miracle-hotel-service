package com.miracle.keoffor.repository;

import com.miracle.keoffor.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String role_user);

    boolean existsByName(String role);
}
