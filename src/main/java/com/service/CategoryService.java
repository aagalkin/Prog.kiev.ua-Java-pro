package com.service;

import com.entity.Category;
import com.entity.Product;
import com.repo.CategoryRepo;
import com.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ProductRepo productRepo;

    @Transactional
    public boolean addCategory(String name){
        if (name.isEmpty()) return false;
        Category category = categoryRepo.findByName(name);

        if (category != null){
            return false;
        }

        category = new Category(name);
        categoryRepo.save(category);
        return true;
    }

    @Transactional
    public List<Category> findAll(){
        return categoryRepo.findAll();
    }

    @Transactional
    public boolean delete(Long id){
        if (id == null || id < 1) return false;
        Category category = categoryRepo.getOne(id);
        if (category == null) return false;
        List<Product> products = category.getProducts();
        for (Product product : products){
            product.setCategory(null);
            productRepo.save(product);
        }
        categoryRepo.deleteById(id);
        return true;
    }

    @Transactional
    public List<Category> findAllWhereNameContains(String name){
        if (name.isEmpty()) return categoryRepo.findAll();
        List<Category> categories = new ArrayList<>();
        List<Category> list = categoryRepo.findAll();
        for (Category category : list){
            if (category.getName().toLowerCase().contains(name.toLowerCase())){
                categories.add(category);
            }
        }
        return categories;
    }

    @Transactional
    public Category getOne(Long id){
        return categoryRepo.getOne(id);
    }

    @Transactional
    public boolean update(Long id, String name){
        if (id == null || id < 1 || name.isEmpty()) return false;
        Category category = categoryRepo.getOne(id);
        if (category == null) return false;
        category.setName(name);
        categoryRepo.save(category);
        return true;
    }
}
