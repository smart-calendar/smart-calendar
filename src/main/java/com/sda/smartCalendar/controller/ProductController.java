package com.sda.smartCalendar.controller;

import com.sda.smartCalendar.controller.modelDTO.ProductDTO;
import com.sda.smartCalendar.domain.model.Shopping;
import com.sda.smartCalendar.domain.model.User;
import com.sda.smartCalendar.service.ProductService;
import com.sda.smartCalendar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping("/shoppinglist")
    public String showList(@ModelAttribute("one") ProductDTO productDTO, Model model, Principal principal){
        model.addAttribute("loggedInUser", userService.findByEmail(principal.getName()));
        model.addAttribute("productList",productService.getAllProductsByUser(principal.getName()));
        model.addAttribute("popular", Shopping.values());
  //      model.addAttribute("one", productDTO);
        return "shoppingList";
    }

    @PostMapping("/shoppinglist/add")
    public String addProducts(ProductDTO productDTO, Principal principal, Model model){
        model.addAttribute("loggedInUser", userService.findByEmail(principal.getName()));
        productService.addProduct(productDTO, principal);
        model.addAttribute("popular", Shopping.values());
        model.addAttribute("one", productDTO);
        return "redirect:/shoppinglist";
    }

    @Transactional
    @GetMapping("/deleteproduct/{product}")
    public String deleteProduct(@PathVariable("product") UUID id){
        productService.deleteProduct(id);
        return "redirect:/shoppinglist";
    }

    @Transactional
    @GetMapping("/deletelist")
    public String deleteList(){
        productService.deleteList();
        return "redirect:/shoppinglist";
    }

}
