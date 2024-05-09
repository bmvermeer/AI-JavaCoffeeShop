package org.workshop.coffee.repository;

import org.workshop.coffee.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.List;
import java.util.Locale;

@Repository
public class SearchRepository {

    @Autowired
    EntityManager em;

    @Autowired
    DataSource dataSource;

    public List<Product> searchProduct (String input) {
        //make the input lowercase
        var lowerInput = input.toLowerCase(Locale.ROOT);
        //search the product table for names or descriptions that contain the input
        String query = "Select * from Product where lower(description) like '%" + lowerInput + "%' OR lower(product_name) like '%" + lowerInput + "%'";
        //return the results
        var resultList = (List<Product>) em.createNativeQuery(query, Product.class).getResultList();
        return resultList;
    }

}
