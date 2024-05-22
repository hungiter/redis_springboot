package com.example.pub.controller;

import com.example.pub.models.Product;
import com.example.pub.models.ProductForm;
import com.example.pub.models.ProductRespository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductFormController implements WebMvcConfigurer {

    private final List<String> productList = new ArrayList<>();
    private final List<String> errors = new ArrayList<>();

    @Autowired
    private ProductRespository productRespository;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("./result").setViewName("result");
    }

    @GetMapping("/")
    public String showForm(Model model, ProductForm productForm, String error) {
        model.addAttribute("productList", productList);
        model.addAttribute("errors", errors);
        return "productForm";
    }

    @PostMapping("/")
    public String checkProductInfo(@Valid ProductForm productForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                String tmp = error.toString() + " : " + error.getDefaultMessage();
                errors.add(tmp);
            });
            return "redirect:./";
        }

        Product product = new Product(productForm.getName(), productForm.getPrice(), productForm.getQuantity());
        productRespository.save(product);
        errors.clear();
        productList.add(productForm.toString());
        return "redirect:./";
//        return "redirect:./result";
    }
}