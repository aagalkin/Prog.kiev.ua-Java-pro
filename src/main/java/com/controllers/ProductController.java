package com.controllers;

import com.entity.Client;
import com.entity.Product;
import com.entity.Warehouse;
import com.service.CategoryService;
import com.service.ClientService;
import com.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private Warehouse warehouse;

    @Autowired
    private Client guest;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public String createProduct(@RequestParam String name,
                                @RequestParam Double price,
                                @RequestParam(name = "category") Long categoryId,
                                @RequestParam(required = false) String desc,
                                @RequestParam(required = false, name = "photo") MultipartFile img){
        boolean success = productService.create(name, price, categoryId, desc, img);
        if (!success) return "redirect:/admin?product&error";
        return "redirect:/admin?product&success";
    }

    @PostMapping("/update")
    public String update(@RequestParam String name,
                         @RequestParam Double price,
                         @RequestParam Long id,
                         @RequestParam(name = "category", required = false) Long categoryId,
                         @RequestParam Integer count){
        boolean success = productService.update(name, price, id, categoryId, count);
        if (!success) return "redirect:/admin?product&error";
        return "redirect:/admin?product&success";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id){
        boolean success = productService.delete(id);
        if (!success) return "redirect:/admin?product&error";
        return "redirect:/admin?product&success";
    }

    @GetMapping("/get")
    public String getProduct(Principal principal, Model model,
                             @RequestParam Long id){
        Product product = productService.getOne(id);
        if (principal != null){
            Client client = clientService.getClientByEmail(principal.getName());
            model.addAttribute("logged", true);
            model.addAttribute("client", client);
            boolean inContainer;
            if (client.getContainer().get(product) == null) inContainer = false;
            else inContainer = true;
            model.addAttribute("inContainer", inContainer);
        } else {
            model.addAttribute("logged", false);
            model.addAttribute("client", guest);
            model.addAttribute("inContainer", false);
        }

        if (product == null) return "redirect:/index";
        boolean inWarehouse;
        if (warehouse.getProducts().get(product) > 0) inWarehouse = true;
        else inWarehouse = false;
        model.addAttribute("inWarehouse", inWarehouse);
        model.addAttribute("product", product.toDTO());
        model.addAttribute("categories", categoryService.findAll());
        return "product_info";
    }
}
