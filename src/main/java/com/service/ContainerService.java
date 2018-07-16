package com.service;

import com.entity.Client;
import com.entity.Container;
import com.entity.Product;
import com.repo.ContainerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContainerService {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ContainerRepo containerRepo;

    @Transactional
    public boolean add(Long client_id, Long product_id, Integer count){
        if (count < 1) return false;
        Product product = productService.getOne(product_id);
        Client client = clientService.getOne(client_id);
        Container container = client.getContainer();
        if (container.contains(product)) {
            container.add(product, container.get(product) + count);
        } else {
            container.add(product, count);
            container.setCount(container.getCount() + 1);
        }
        containerRepo.save(container);
        return true;
    }

    @Transactional
    public boolean remove(Long client_id, Long product_id) {
        Client client = clientService.getOne(client_id);
        Product product = productService.getOne(product_id);
        Container container = client.getContainer();
        if (container.contains(product)) {
            container.remove(product);
            container.setCount(container.getCount() - 1);
            containerRepo.save(container);
        }
        return true;
    }

}
