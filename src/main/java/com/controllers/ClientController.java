package com.controllers;

import com.entity.Client;
import com.entity.Role;
import com.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/admin_update")
    public String update(@RequestParam Long id,
                         @RequestParam(required = false) String role){
        boolean success = clientService.update(id,  null, null, null, null, role);
        if (!success) return "redirect:/admin?user&error";
        return "redirect:/admin?user&success";
    }

    @PostMapping("/update")
    public String update(Principal principal, Model model,
                         @RequestParam Long id,
                         @RequestParam String name,
                         @RequestParam String surname,
                         @RequestParam Integer phone,
                         @RequestParam String city){

        if (principal==null)return "redirect:/index";
        Client client = clientService.getClientByEmail(principal.getName());
        model.addAttribute("client", client);

        boolean success = clientService.update(id, name, surname, phone, city, null);
        model.addAttribute("success", success);
        return "profile";
    }


    @PostMapping("/password_change")
    public String changePassword(Principal principal, Model model,
                                 @RequestParam Long id,
                                 @RequestParam(name = "old_password") String oldPassword,
                                 @RequestParam(name = "new_password") String newPassword,
                                 @RequestParam(name = "repeat_new_password") String repeatPassword){

        if (principal==null)return "redirect:/index";
        Client client = clientService.getClientByEmail(principal.getName());
        model.addAttribute("client", client);

        if (!newPassword.equals(repeatPassword)) {
                model.addAttribute("success", false);
                model.addAttribute("error", "Пароли не совпадают");
            return "profile";
        }

        boolean success = clientService.editPassword(id, oldPassword, newPassword);
        model.addAttribute("success", success);
        return "profile";
    }
}
