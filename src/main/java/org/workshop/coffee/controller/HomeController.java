package org.workshop.coffee.controller;

import org.workshop.coffee.domain.Product;
import org.workshop.coffee.repository.SearchRepository;
import org.workshop.coffee.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.List;
import java.util.Locale;

@Controller
public class HomeController {

    private ProductService productService;
    private SearchRepository searchRepository;

    @Autowired
    public HomeController(ProductService productService, SearchRepository searchRepository) {
        this.productService = productService;
        this.searchRepository = searchRepository;
    }

    @GetMapping({"/", "/index", "/home"})
    public String homePage(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "index";
    }

    @Autowired
    EntityManager em;

    @PostMapping("/")
    public String searchProducts(Model model, @RequestParam String input) {
        return "index";
    }
}
