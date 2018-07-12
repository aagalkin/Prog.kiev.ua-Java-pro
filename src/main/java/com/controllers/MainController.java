package com.controllers;

import java.security.Principal;
import java.util.*;

import com.entity.*;
import com.repo.CategoryRepo;
import com.repo.ClientRepo;
import com.repo.OrderRepo;
import com.repo.ProductRepo;
import com.service.ClientService;
import com.service.OrderService;
import com.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class MainController {

    @Autowired
    private Warehouse warehouse;

    @Autowired
    private ProductService productService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private Client guest;

    @GetMapping("/")
    public String getIndex(Principal principal, Model model,
                           @RequestParam(required = false) String category,
                           @RequestParam(required = false) String search){
        if (principal != null){
            Client client = clientService.getClientByEmail(principal.getName());
            model.addAttribute("logged", true);
            model.addAttribute("client", client);
            model.addAttribute("container", client.getContainer());
        } else {
            model.addAttribute("logged", false);
            model.addAttribute("client", guest);
        }

        List<Category> categories = categoryRepo.findAll();
        model.addAttribute("categories", categories);

        List<ProductDTO> products = new ArrayList<>();
        if (category != null){
            for (Product product : categoryRepo.findByName(category).getProducts()){
                products.add(product.toDTO());
            }
        }else if (search != null){
            //TODO
            //как искать?
            products = null;
        } else {
            products = productService.findAllDTO();
        }
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("products", products);
        return "index";
    }

    @GetMapping("/index")
    public String getIndexx(Principal principal, Model model,
                           @RequestParam(required = false) String category,
                           @RequestParam(required = false) String search){
        if (principal != null){
            Client client = clientService.getClientByEmail(principal.getName());
            model.addAttribute("logged", true);
            model.addAttribute("client", client);
            model.addAttribute("container", client.getContainer());
        } else {
            model.addAttribute("logged", false);
            model.addAttribute("client", guest);
        }

        List<Category> categories = categoryRepo.findAll();
        model.addAttribute("categories", categories);

        List<ProductDTO> products = new ArrayList<>();
        if (category != null){
            for (Product product : categoryRepo.findByName(category).getProducts()){
                products.add(product.toDTO());
            }
        }else if (search != null){
            //TODO
            //как искать?
            products = null;
        } else {
            products = productService.findAllDTO();
        }
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("products", products);
        return "index";
    }
}
