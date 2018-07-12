package com.service;

import com.Tools;
import com.entity.*;
import com.repo.ClientRepo;
import com.repo.OrderRepo;
import com.repo.ProductCountRepo;
import com.repo.WarehouseRepo;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private EntityManager em;

    @Autowired
    private Warehouse warehouse;

    @Autowired
    private WarehouseRepo warehouseRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ProductService productService;

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private ProductCountRepo productCountRepo;

    @Autowired
    private WarehouseService warehouseService;

    @Transactional
    public boolean create(Long client_id, Long[] product_id, Integer[] count, Address address){
        if (client_id == null || product_id == null || count == null || product_id.length != count.length) return false;
        Client client = clientRepo.getOne(client_id);
        double totalPrice = 0;
        List<ProductCount> products = new ArrayList<>();
        for (int i = 0; i < product_id.length; i++){
            Product product = productService.getOne(product_id[i]);
            totalPrice += product.getPrice() * count[i];
            ProductCount productCount = new ProductCount(product, count[i]);
            products.add(productCount);
            if (warehouse.getProducts().get(product) < count[i]) return false;
            productCountRepo.save(productCount);
            warehouse.getProducts().put(product, warehouse.getProducts().get(product) - count[i]);
        }

        Order order = new Order(client, totalPrice, products);
        order.setDate(new Date());
        order.setStatus(Status.IN_PROGRESS);
        order.setDelivery(address);
        client.addOrder(order);
        clientRepo.save(client);
        orderRepo.save(order);
        warehouseRepo.save(warehouse);
        return true;
    }

    @Transactional
    public Order getOne(Long id){
        return orderRepo.getOne(id);
    }

    @Transactional
    public List<Order> searchById(String id){
        if (id == null || id.isEmpty() || (!Tools.isDigit(id) && !id.equals("complete") && !id.equals("terminated") && !id.equals("in_progress"))) return orderRepo.findAll();
        List<Order> orders = new ArrayList<>();
        if (Tools.isDigit(id)){
            Order order;
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Order> criteria = builder.createQuery(Order.class);
            Root<Order> root = criteria.from(Order.class);
            criteria.select(root);
            criteria.where(builder.equal(root.get("id"), id));

            try {
                order = em.createQuery(criteria).getSingleResult();
                orders.add(order);
            }catch (NoResultException e){
                return orderRepo.findAll();
            }
        }else {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Order> criteria = builder.createQuery(Order.class);
            Root<Order> root = criteria.from(Order.class);
            criteria.select(root);
            switch (id) {
                case "complete":
                    criteria.where(builder.equal(root.get("status"), Status.COMPLETE));
                    break;
                case "terminated":
                    criteria.where(builder.equal(root.get("status"), Status.TERMINATED));
                    break;
                case "in_progress":
                    criteria.where(builder.equal(root.get("status"), Status.IN_PROGRESS));
                    break;
            }
            try {
                orders = em.createQuery(criteria).getResultList();
            } catch (NoResultException e){
                return orderRepo.findAll();
            }
        }
        return orders;
    }

    @Transactional
    public void save(Order order){
        orderRepo.save(order);
    }

    @Transactional
    public void setData(Long order_id, Long[] product_id, Integer[] count){
        Order order = orderRepo.getOne(order_id);
        order.cleanProducts(warehouseService, warehouse);
        Double totalPrice = 0d;
        List<ProductCount> products = new ArrayList<>();
        for (int i = 0; i < product_id.length; i++){
            Product product = productService.getOne(product_id[i]);
            totalPrice += product.getPrice() * count[i];
            ProductCount productCount = new ProductCount(product, count[i]);
            products.add(productCount);
            if (warehouse.getProducts().get(product) < count[i]) return;
            productCountRepo.save(productCount);
            warehouse.getProducts().put(product, warehouse.getProducts().get(product) - count[i]);
        }
        order.setProducts(products);
        order.setTotalPrice(totalPrice);
        orderRepo.save(order);
    }
}
