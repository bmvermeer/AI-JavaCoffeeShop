package org.workshop.coffee.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.workshop.coffee.domain.Product;
import org.workshop.coffee.domain.ProductType;
import org.workshop.coffee.service.PersonService;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Locale;

@Component
public class Util {


    @Autowired
    EntityManager em;

    @Autowired
    DataSource dataSource;

    @Autowired
    private PersonService personService;

    public List<Product> searchProduct(String input) {
        var lowerInput = input.toLowerCase(Locale.ROOT);
        String query = "Select * from Product where lower(description) like '%" + lowerInput + "%' OR lower(product_name) like '%" + lowerInput + "%'";
        var resultList = (List<Product>) em.createNativeQuery(query, Product.class).getResultList();
        return resultList;
    }

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";
    public String uploadImage(Model model, @RequestParam("image") MultipartFile file, Principal principal) throws IOException {
        var name = file.getOriginalFilename().replace(" ", "_");
        var fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, name);
        Files.write(fileNameAndPath, file.getBytes());
        model.addAttribute("msg", "Uploaded images: " + name);

        if (principal == null) {
            model.addAttribute("message", "ERROR");
            return "person/upload";
        }

        var user = principal.getName();
        var person = personService.findByUsername(user);

        person.setProfilePic(name);
        personService.savePerson(person);
        return "person/upload";
    }

    private void buildProductPage(String productName, String desc, ProductType productType, Double price, PrintWriter writer) throws IOException {
        String head = "<html>\n" +
                "  <head lang=\"en\">\n" +
                "    <title>CoffeeShop</title>\n" +
                "     \n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "    <link href=\"/webjars/bootstrap/3.3.4/css/bootstrap.min.css\" rel=\"stylesheet\" media=\"screen\" />\n" +
                "    <link href=\"/css/style.css\" rel=\"stylesheet\" media=\"screen\" />\n" +
                "    <script src=\"/webjars/jquery/2.1.4/jquery.js\"></script>\n" +
                "    <script src=\"/webjars/bootstrap/3.3.4/js/bootstrap.js\"></script>\n" +
                "   \n" +
                "  </head>\n" +
                "  <body><div class=\"container\"><div class=\"panel panel-default\">";

        String foot = "  </div></div></body>\n" +
                "</html>";

        writer.write(head);

        writer.write("<div class=\"panel-heading\"><h1>" + productName + "</h1></div>");

        String output = "<div class=\"panel-body\">" +
                "<ul>" +
                "<li>%s</li>" +
                "<li>%s</li>" +
                "<li>%s</li>" +
                "</ul>" +
                "</div>";

        writer.write(String.format(output, desc, productType, price));
        writer.write(foot);


    }


}
