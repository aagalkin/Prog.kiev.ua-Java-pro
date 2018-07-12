package com.controllers;

import com.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public String create(@RequestParam String name){
        boolean success = categoryService.addCategory(name);
        if (!success) return "redirect:/admin?category&error";
        return "redirect:/admin?category&success";
    }

    @PostMapping("/update")
    public String update(@RequestParam Long id, @RequestParam String name){
        boolean success = categoryService.update(id, name);
        if (!success) return "redirect:/admin?category&error";
        return "redirect:/admin?category&success";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id){
        boolean success = categoryService.delete(id);
        if (!success) return "redirect:/admin?category&error";
        return "redirect:/admin?category&success";
    }
}
