package com.controllers;

import com.entity.*;
import com.repo.ContainerRepo;
import com.service.ClientService;
import com.service.OrderService;
import com.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ContainerRepo containerRepo;

    @Autowired
    private Warehouse warehouse;

    @Autowired
    private WarehouseService warehouseService;

    @PostMapping("/create")
    public String createOrder(Principal principal, Model model,
                              @RequestParam(name = "name") String clientName,
                              @RequestParam(name = "surname") String clientSurname,
                              @RequestParam(name = "phone") Integer clientPhone,
                              @RequestParam(required = false) String clientEmail,
                              @RequestParam Long[] product_id,
                              @RequestParam Integer[] counts, @RequestParam(name = "del_type") String deliveryType,
                              @RequestParam(required = false) String street,
                              @RequestParam(required = false) String house,
                              @RequestParam(required = false) String flat){
        Client client = clientService.getClientByEmail(principal.getName());
        model.addAttribute("client", client);

        Address address;
        if(deliveryType.equals("delivery")) {
            address = new Address();
            address.setStreet(street);
            address.setHouse(house);
            address.setFlat(flat);
        } else {
            address = new Address();
            address.setStreet("самовывоз");
            address.setHouse("самовывоз");
            address.setFlat("самовывоз");
        }

        Client orderClient = null;
        if (clientEmail == null){
            if (clientName != null && clientSurname != null && clientPhone != null){
                orderClient = new Client();
                orderClient.setName(clientName);
                orderClient.setSurname(clientSurname);
                orderClient.setPhone(clientPhone);
                orderClient.setRole(Role.GUEST);
                orderClient.setOrders(new ArrayList<>());
                clientService.save(orderClient);
            }
        }else {
            orderClient = clientService.getClientByEmail(clientEmail);
        }

        boolean success = orderService.create(orderClient.getId(), product_id, counts, address);
        if (!success){
            return "redirect:/admin?error";
        }
        return "redirect:/admin?success";
    }

    @PostMapping("/user_create")
    public String userCreate(Principal principal, Model model,
                             @RequestParam Long[] product_id,
                             @RequestParam Integer[] count,
                             @RequestParam(name = "del_type") String deliveryType,
                             @RequestParam(required = false) String street,
                             @RequestParam(required = false) String house,
                             @RequestParam(required = false) String flat){
        Client client = clientService.getClientByEmail(principal.getName());
        Address address;
        if(deliveryType.equals("delivery")) {
            address = new Address();
            address.setStreet(street);
            address.setHouse(house);
            address.setFlat(flat);
        } else {
            address = new Address();
            address.setStreet("самовывоз");
            address.setHouse("самовывоз");
            address.setFlat("самовывоз");
        }
        boolean success = orderService.create(client.getId(), product_id, count, address);
        String msg;
        if (!success) {
            msg = "Не удалось создать заказ";
        }
        else {
            msg = "Заказ успешно создан";
            Container container = client.getContainer();
            container.clear();
            containerRepo.save(container);
        }
        model.addAttribute("msg", msg);
        model.addAttribute("client", client);
        List<ProductDTO> products = new ArrayList<>();
        for (Product product : client.getContainer().getProducts().keySet()){
            products.add(product.toDTO());
        }
        if (products.size() > 0){
            model.addAttribute("products", products);
        }
        return "client_orders";
    }

    @GetMapping("/get")
    public String getOrder(Principal principal, Model model,
                           @RequestParam Long order_id){
        Client client = clientService.getClientByEmail(principal.getName());
        model.addAttribute("client", client);
        Order order = orderService.getOne(order_id);
        if (!client.getOrders().contains(order)){
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

        model.addAttribute("order", order);
        return "show_order";
    }

    @PostMapping("/update_status")
    public String setStatus(@RequestParam Long order_id,
                                 @RequestParam String status){
        Order order = orderService.getOne(order_id);
        Status st = Status.IN_PROGRESS;
        if (status.equals("TERMINATED")) {
            st = Status.TERMINATED;
            order.cleanProducts(warehouseService);
        }
        else if (status.equals("COMPLETE")) st = Status.COMPLETE;
        order.setStatus(st);
        orderService.save(order);
        return "redirect:/admin/search?order="+order_id;
    }

    @PostMapping("/update_data")
    public String setData(@RequestParam Long order_id,
                          @RequestParam Long[] product_id,
                          @RequestParam Integer[] count){
        orderService.setData(order_id, product_id, count);
        return "redirect:/admin/search?order="+order_id;
    }

    @PostMapping("/update_delivery")
    public String setDelivery(@RequestParam Long order_id,
                              @RequestParam String street,
                              @RequestParam String house,
                              @RequestParam String flat){
        Order order = orderService.getOne(order_id);
        order.getDelivery().setStreet(street);
        order.getDelivery().setHouse(house);
        order.getDelivery().setFlat(flat);
        orderService.save(order);
        return "redirect:/admin/search="+order_id;
    }
}
