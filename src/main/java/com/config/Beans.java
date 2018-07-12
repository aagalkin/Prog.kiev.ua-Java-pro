package com.config;

import com.entity.Client;
import com.entity.Product;
import com.entity.Role;
import com.entity.Warehouse;
import com.repo.WarehouseRepo;
import com.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;

@Configuration
public class Beans {

    @Autowired
    private EntityManager em;

    @Autowired
    private WarehouseRepo warehouseRepo;

    @Bean
    public Client getGuest(){
        Client client = new Client();
        client.setName("Гость");
        client.setRole(Role.GUEST);
        return client;
    }

    @Bean()
    public Warehouse getWarehouse(){
        Warehouse warehouse;
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Warehouse> criteria = builder.createQuery(Warehouse.class);
        Root<Warehouse> root = criteria.from(Warehouse.class);
        criteria.select(root);

        try {
            warehouse = em.createQuery(criteria).getSingleResult();
        }
        catch (NoResultException e){
            warehouse = new Warehouse();
            warehouse.setProducts(new HashMap<>());
            warehouseRepo.save(warehouse);
        }
        return warehouse;
    }
}
