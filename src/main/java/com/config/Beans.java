package com.config;

import com.entity.*;
import com.repo.ClientRepo;
import com.repo.ContainerRepo;
import com.repo.WarehouseRepo;
import com.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;

@Configuration
public class Beans {

    @Autowired
    private EntityManager em;

    @Autowired
    private WarehouseRepo warehouseRepo;

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private ContainerRepo containerRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public Client getGuest(){
        Client admin = clientRepo.findByEmail("admin");
        if (admin == null){
            admin = new Client();
            admin.setEmail("admin");
            admin.setName("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setPhone(12345);
            admin.setRole(Role.ADMIN);
            admin.setSurname("");
            admin.setOrders(new ArrayList<>());
            clientRepo.save(admin);
            Container container = new Container();
            container.setClient(admin);
            container.setProducts(new HashMap<>());
            container.setCount(0);
            admin.setContainer(container);
            containerRepo.save(container);
            clientRepo.save(admin);
            containerRepo.save(container);
            clientRepo.save(admin);
        }
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
