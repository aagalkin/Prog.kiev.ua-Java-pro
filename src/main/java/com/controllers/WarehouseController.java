package com.controllers;

import com.entity.Client;
import com.service.ClientService;
import com.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/warehouse")
public class WarehouseController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private WarehouseService warehouseService;

    @GetMapping("/income")
    public String income(Principal principal, Model model,
                         @RequestParam Long products_id[],
                         @RequestParam Integer counts[]){
        Client client = clientService.getClientByEmail(principal.getName());
        model.addAttribute("client", client);

        boolean success = warehouseService.income(products_id, counts);
        //TODO
        if (!success) return "";
        return "";
    }
}
