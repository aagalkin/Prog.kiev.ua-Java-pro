package com.repo;

import com.entity.Container;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContainerRepo extends JpaRepository<Container, Long> {
}
