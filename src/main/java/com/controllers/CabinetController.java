package com.controllers;

import com.entity.Client;
import com.entity.Order;
import com.entity.Product;
import com.entity.ProductDTO;
import com.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cabinet")
public class CabinetController {

    @Autowired
    private ClientService clientService;

    @GetMapping()
    public String getCabinet(Principal principal, Model model){
        if (principal==null)return "redirect:/index";
        Client client = clientService.getClientByEmail(principal.getName());
        model.addAttribute("client", client);

        List<ProductDTO> products = new ArrayList<>();
        for (Product product : client.getContainer().getProducts().keySet()){
            products.add(product.toDTO());
        }
        if (products.size() > 0){
            model.addAttribute("products", products);
        }
        return "profile";
    }

    @GetMapping("/profile")
    public String getProfile(Principal principal, Model model){
        if (principal==null)return "redirect:/index";
        Client client = clientService.getClientByEmail(principal.getName());
        model.addAttribute("client", client);

        List<ProductDTO> products = new ArrayList<>();
        for (Product product : client.getContainer().getProducts().keySet()){
            products.add(product.toDTO());
        }
        if (products.size() > 0){
            model.addAttribute("products", products);
        }
        return "profile";
    }

    @GetMapping("/orders")
    public String getOrders(Principal principal, Model model){
        if (principal==null)return "redirect:/index";
        Client client = clientService.getClientByEmail(principal.getName());
        model.addAttribute("client", client);
        List<Order> orders = client.getOrders();
        model.addAttribute("orders", orders);
        List<ProductDTO> products = new ArrayList<>();
        for (Product product : client.getContainer().getProducts().keySet()){
            products.add(product.toDTO());
        }
        if (products.size() > 0){
            model.addAttribute("products", products);
        }
        return "client_orders";
    }

    @GetMapping("/container")
    public String getContain(Principal principal, Model model){
        if (principal==null)return "redirect:/index";
        Client client = clientService.getClientByEmail(principal.getName());
        model.addAttribute("client", client);
        List<Order> orders = client.getOrders();
        model.addAttribute("orders", orders);
        List<ProductDTO> products = new ArrayList<>();
        for (Product product : client.getContainer().getProducts().keySet()){
            products.add(product.toDTO());
        }
        if (products.size() > 0){
            model.addAttribute("products", products);
        }
        model.addAttribute("toContainer", true);
        return "client_orders";
    }
}
