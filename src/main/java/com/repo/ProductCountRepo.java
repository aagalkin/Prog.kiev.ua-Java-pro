package com.repo;

import com.entity.ProductCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCountRepo extends JpaRepository<ProductCount, Long> {
}
