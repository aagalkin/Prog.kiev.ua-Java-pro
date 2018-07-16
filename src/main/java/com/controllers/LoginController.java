package com.controllers;
import java.security.Principal;

import com.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/registration")
    public String addNewClient(@RequestParam() String name,
                               @RequestParam(required = false, defaultValue = "")String surname,
                               @RequestParam() String email,
                               @RequestParam String password,
                               @RequestParam(name = "conf_password") String confPassword,
                               @RequestParam(required = false, defaultValue = "") Integer phone,
                               @RequestParam(required = false, defaultValue = "") String city,
                               Model model){
        if(name.isEmpty() || email.isEmpty() || password.isEmpty() || confPassword.isEmpty()){
            model.addAttribute("error", "Неоходимо заполнить поля отмеченные '*'");
            return "registration";
        }

        if (!password.equals(confPassword)){
            model.addAttribute("error", "Пароли не совпадают");
            return "registration";
        }

        if (!clientService.addNewClient(name, surname, email, passwordEncoder.encode(password), phone, city)){
            model.addAttribute("error", "Пользователь с таким именем уже зарегестрирован");
            return "registration";
        }

        return "reg_successful";
    }

    @GetMapping("/registration")
    public String toReg(Principal principal){
        if (principal != null){
            return "redirect:/index";
        }
        return "registration";
    }

    @GetMapping("/login")
    public String getLogin(Principal principal){
        if (principal != null){
            return "redirect:/index";
        }
        return "login";
    }
}
