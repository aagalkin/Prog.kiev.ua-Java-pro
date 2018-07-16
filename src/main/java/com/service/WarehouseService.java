package com.service;

import com.entity.Product;
import com.entity.Warehouse;
import com.repo.WarehouseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;


@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepo warehouseRepo;

    @Autowired
    private ProductService productService;

    @Autowired
    private Warehouse warehouse;

    @Transactional
    public boolean income(Long products_id[], Integer counts[]){
        if (products_id == null || counts == null || products_id.length != counts.length) return false;
        Map<Product, Integer> products = new HashMap<>();
        for (int i = 0; i < products_id.length; i++){
            Product product = productService.getOne(products_id[i]);
            Integer count = warehouse.getProducts().get(product) + counts[i];
            products.put(product, count);
        }
        warehouse.getProducts().putAll(products);
        warehouseRepo.save(warehouse);
        return true;
    }

    @Transactional
    public void save(Warehouse warehouse){
        warehouseRepo.save(warehouse);
    }
}
