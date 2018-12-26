package com.rarelittlebeastie.onixweb.onixApp.controller;

import com.rarelittlebeastie.onixweb.onixApp.model.Products;
import com.rarelittlebeastie.onixweb.onixApp.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class OnixController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String monitorPage(Model model) {
        Products products = productService.getProducts();
        if (products == null){
            log.info("No Product Info Found");
            products = new Products();
        }
        model.addAttribute("products", products);
        return "onix";
    }
}