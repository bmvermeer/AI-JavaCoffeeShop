package org.workshop.coffee.controller;

import org.workshop.coffee.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;

@Controller
public class HomeController {

    private ProductService productService;

    @Autowired
    public HomeController(ProductService productService) {
        this.productService = productService;
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
        // search product by name or description
        var lowerInput = input.toLowerCase(Locale.ROOT);
        String query = "SELECT * FROM Product WHERE lower(description) LIKE '%" + lowerInput + "%' OR lower(product_name) LIKE '%" + lowerInput + "%'";
        List<Product> resultList = em.createNativeQuery(query, Product.class).getResultList();
        if (resultList.isEmpty()) {
            model.addAttribute("message", "No products found for: " + input);
        } else {
            model.addAttribute("products", resultList);
        }
        return "index";
    }
}
