package com.controllers;

import com.Tools;
import com.entity.*;
import com.service.CategoryService;
import com.service.ClientService;
import com.service.OrderService;
import com.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private Warehouse warehouse;

    @GetMapping()
    public String getPage(Model model, Principal principal,
                          @RequestParam(required = false) String category,
                          @RequestParam(required = false) String product,
                          @RequestParam(required = false) String user,
                          @RequestParam(required = false) String order){
        Client client = clientService.getClientByEmail(principal.getName());
        model.addAttribute("client", client);

        if (category != null){
            Set<Category> categories = new HashSet<>(categoryService.findAllWhereNameContains(category));
            model.addAttribute("categories", categories);
        }else if (product != null){
            List<Product> products = productService.findAllWhereNameContains(product);
            if (Tools.isDigit(product)){
                Product pr = productService.getOne(Long.parseLong(product));
                if (pr != null){
                    products.add(pr);
                }
            }
            model.addAttribute("products", products);
            for (Product product1 : products){
                System.out.println(warehouse.getProducts().get(product1));
            }
            model.addAttribute("warehouse", warehouse);
        }else if (user != null){
            Set<Client> clients = new HashSet<>(clientService.findAllWhereContains(user));
            model.addAttribute("users", clients);
        }else if (order != null){
            List<Order> orders = orderService.searchById(order);
            model.addAttribute("orders", orders);
        }
        return "admin";
    }

    @GetMapping("/search")
    public String search(Principal principal, Model model,
                         @RequestParam(required = false) Long category,
                         @RequestParam(required = false) Long product,
                         @RequestParam(required = false) Long user,
                         @RequestParam(required = false) Long order){
        Client client = clientService.getClientByEmail(principal.getName());
        model.addAttribute("client", client);

        if (category != null){
            Category ctg = categoryService.getOne(category);
            model.addAttribute("category", ctg);
            model.addAttribute("products", ctg.getProducts());
        }else if (product != null){
            Product prd = productService.getOne(product);
            List<Category> categories = categoryService.findAll();
            model.addAttribute("count", warehouse.getProducts().get(prd));
            model.addAttribute("product", prd);
            model.addAttribute("categories", categories);
        }else if (user != null){
            Client cln = clientService.getOne(user);
            model.addAttribute("user", cln);
            List<Role> roles = Arrays.asList(Role.values());
            model.addAttribute("roles", roles);
        }else if (order != null){
            Order ord = orderService.getOne(order);
            model.addAttribute("order", ord);
            model.addAttribute("warehouse", warehouse);
        }
        return "admin_edit";
    }

    @GetMapping("/create")
    public String create(Principal principal, Model model,
                         @RequestParam(required = false) String category,
                         @RequestParam(required = false) String product,
                         @RequestParam(required = false) String order,
                         @RequestParam(required = false) String name,
                         @RequestParam(required = false, defaultValue = "") String surname,
                         @RequestParam(required = false) Integer phone,
                         @RequestParam(required = false) Client user,
                         @RequestParam(required = false) List<ProductCount> products){
        Client client = clientService.getClientByEmail(principal.getName());
        model.addAttribute("client", client);

        if (category == null && product == null && order == null){
            return "admin";
        }

        if (product != null){
            List<Category> categories = categoryService.findAll();
            model.addAttribute("categories", categories);
        }

        if (order != null){
            if (user != null){
                if (name != null || phone != null){
                    user.setName(name);
                    user.setPhone(phone);
                    user.setSurname(surname);
                }
                model.addAttribute("user", user);
            }else if (name != null && phone != null){
                Client usr = new Client();
                usr.setName(name);
                usr.setSurname(surname);
                usr.setPhone(phone);
                model.addAttribute("user", usr);
            }

            if (products != null){
                model.addAttribute("products", products);
            }
        }
        return "admin_create";
    }

    @PostMapping("/new_order")
    public String createOrder(Principal principal, Model model,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false, defaultValue = "") String surname,
                              @RequestParam(required = false) Integer phone,
                              @RequestParam(required = false) Long product[],
                              @RequestParam(required = false) Integer count[]){
        Client client = clientService.getClientByEmail(principal.getName());
        model.addAttribute("client", client);

        List<Product> productList = productService.findAll();
        model.addAttribute("product_list", productList);

        if (name != null && phone != null) {
            Client usr = new Client();
            usr.setName(name);
            usr.setSurname(surname);
            usr.setPhone(phone);
            usr.setRole(Role.GUEST);
            model.addAttribute("user", usr);
        }

        List<ProductCount> products = null;
        if (product != null && count != null && product.length == count.length){
            products = new ArrayList<>();
            for (int i = 0; i < product.length; i++){
                products.add(new ProductCount(productService.getOne(product[i]), count[i]));
            }
        }

        if (products != null){
            model.addAttribute("products", products);
        }

        model.addAttribute("warehouse", warehouse);

        return "admin_order_create";
    }

    @PostMapping("/check_order")
    public String checkOrder(Principal principal, Model model,
                             @RequestParam(name = "name") String clientName,
                             @RequestParam(name = "surname") String clientSurname,
                             @RequestParam(name = "phone") Integer clientPhone,
                             @RequestParam(required = false) String clientEmail,
                             @RequestParam Long[] product_id,
                             @RequestParam Integer[] counts){
        Client client = clientService.getClientByEmail(principal.getName());
        model.addAttribute("client", client);

        Client orderClient = null;
        if (clientEmail == null){
            if (clientName != null && clientSurname != null && clientPhone != null){
                orderClient = new Client();
                orderClient.setName(clientName);
                orderClient.setSurname(clientSurname);
                orderClient.setPhone(clientPhone);
            }
        }else {
            orderClient = clientService.getClientByEmail(clientEmail);
        }

        if (orderClient != null)
        model.addAttribute("user", orderClient);
        List<ProductCount> products = new ArrayList<>();
        Double totalPrice = 0d;
        for (int i = 0; i < product_id.length; i++){
            Product product = productService.getOne(product_id[i]);
            Integer count = counts[i];
            products.add(new ProductCount(product, count));
            totalPrice += product.getPrice() * count;
        }
        model.addAttribute("total_price", totalPrice);
        model.addAttribute("products", products);
        model.addAttribute("admin", true);
        return "check_order";
    }

    @GetMapping("/new_order")
    public String createOrderPage(Principal principal, Model model){
        Client client = clientService.getClientByEmail(principal.getName());
        model.addAttribute("client", client);
        List<Product> products = productService.findAll();
        model.addAttribute("product_list", products);
        model.addAttribute("warehouse", warehouse);

        return "admin_order_create";
    }
}
