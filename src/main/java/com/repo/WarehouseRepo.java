package com.repo;

import com.entity.Product;
import com.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface WarehouseRepo extends JpaRepository<Warehouse, Long> {
}
