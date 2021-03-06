package com.repo;

import com.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepo extends JpaRepository<Client, Long> {
    Client findByEmail(String email);
}
