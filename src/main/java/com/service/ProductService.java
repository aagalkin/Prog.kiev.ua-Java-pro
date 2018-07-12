package com.service;

import com.entity.*;
import com.repo.CategoryRepo;
import com.repo.ProductRepo;
import com.repo.WarehouseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private EntityManager em;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private WarehouseRepo warehouseRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private Warehouse warehouse;

    @Transactional
    public boolean create(String name, Double price, Long categoryId, String desc, MultipartFile img){
        if (name.isEmpty() || price.isNaN() || price == 0) return false;
        if (categoryId != null) {
            Category category = categoryRepo.getOne(categoryId);
            Product product = new Product(name, price, category);
            product.setDesc(desc);
            if (img == null || img.isEmpty()){
                product.setImg(null);
            } else {
                try {
                    byte[] image = img.getBytes();
                    product.setImg(image);
                } catch (IOException e) {
                    product.setImg(null);
                }
            }
            category.addProduct(product);
            productRepo.save(product);
            categoryRepo.save(category);
            warehouse.addProduct(product);
            warehouseRepo.save(warehouse);
        }else {
            Product product = new Product(name, price, null);

            product.setDesc(desc);
            if (img == null || img.isEmpty()){
                product.setImg(null);
            } else {
                try {
                    byte[] image = img.getBytes();
                    product.setImg(image);
                } catch (IOException e) {
                    product.setImg(null);
                }
            }
            productRepo.save(product);
            warehouse.addProduct(product);
            warehouseRepo.save(warehouse);
        }
        return true;
    }

    @Transactional
    public boolean update(String name, Double price, Long id, Long categoryId, Integer count){
        if (name.isEmpty() || price.isNaN() || price < 0 || count == null || count < 0) return false;
        Product product = productRepo.getOne(id);
        product.setName(name);
        product.setPrice(price);
        Category oldCategory = product.getCategory();
        if (oldCategory != null){
            oldCategory.getProducts().remove(product);
            categoryRepo.save(oldCategory);
        }
        Category newCategory;
        if (categoryId == null) {
            newCategory = null;
        } else {
            newCategory = categoryRepo.getOne(categoryId);
            newCategory.getProducts().add(product);
        }
        product.setCategory(newCategory);
        warehouse.getProducts().put(product, count);
        warehouseRepo.save(warehouse);
        if (newCategory != null) categoryRepo.save(newCategory);
        productRepo.save(product);
        return true;
    }

    public Product getOne(Long id){
        Product product;
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
        Root<Product> root = criteria.from(Product.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get("id"), id));

        try {
            product = em.createQuery(criteria).getSingleResult();
        }catch (NoResultException e){
            product = null;
        }

        return product;
    }

    @Transactional
    public List<Product> findAllWhereNameContains(String name){
        if (name.isEmpty()) return productRepo.findAll();
        List<Product> products = new ArrayList<>();
        List<Product> list = productRepo.findAll();
        for (Product product : list){
            if (product.getName().toLowerCase().contains(name.toLowerCase())){
                products.add(product);
            }
        }
        return products;
    }

    @Transactional
    public boolean delete(Long id) {
        //TODO
        return true;
    }

    @Transactional
    public List<ProductDTO> findAllDTO(){
        List<ProductDTO> products = new ArrayList<>();
        for (Product product : productRepo.findAll()){
            products.add(product.toDTO());
        }
        return products;
    }

    public List<Product> findAll(){
        return productRepo.findAll();
    }
}
