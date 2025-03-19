package com.example.FinalProject.repository;

import com.example.FinalProject.model.Role;
import com.example.FinalProject.model.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

    List<User> findAllByRole(Role role);

    Optional<User> findByEmail(String email);
}
