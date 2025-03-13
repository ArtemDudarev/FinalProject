package com.example.FinalProject.repository;

import com.example.FinalProject.model.Address;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, UUID> {}
