package com.controllers;

import com.entity.Client;
import com.entity.Product;
import com.entity.ProductCount;
import com.service.ClientService;
import com.service.ContainerService;
import com.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/container")
public class ContainerController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private ClientService clientService;

    @PostMapping("/add")
    @ResponseBody
    public String add(@RequestParam Long client_id,
                      @RequestParam Long product_id,
                      @RequestParam Integer count,
                      HttpServletResponse response){
        boolean success = containerService.add(client_id, product_id, count);
        if(success) {
            Client client = clientService.getOne(client_id);
            return "Добавлено в корзину" + client.getContainer().getCount();
        }
        else response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return null;
    }

    @PostMapping("/remove")
    @ResponseBody
    public String remove(@RequestParam Long client_id,
                         @RequestParam Long product_id,
                         HttpServletResponse response){
        boolean success = containerService.remove(client_id, product_id);
        Client client = clientService.getOne(client_id);
        if (!success){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        String count = client.getContainer().getCount().toString();
        return count;
    }

    @PostMapping("/create")
    public String createOrder(Model model,
                              @RequestParam Long[] product_id,
                              @RequestParam Integer[] count){
        Double total_price = 0d;
        List<ProductCount> products = new ArrayList<>();
        for (int i = 0; i < product_id.length; i++){
            Product product = productService.getOne(product_id[i]);
            products.add(new ProductCount(product, count[i]));
            total_price += product.getPrice() * count[i];
        }
        model.addAttribute("products", products);
        model.addAttribute("total_price", total_price);
        return "check_order";
    }
}
