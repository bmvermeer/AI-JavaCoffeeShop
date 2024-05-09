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
        //search the product table for names or descriptions that contain the input with named parameters
        String query = "Select p from Product p where lower(p.description) like :input OR lower(p.productName) like :input";
        var resultList = em.createQuery(query, Product.class)
                .setParameter("input", "%" + lowerInput + "%")
                .getResultList();
        return resultList;
    }

}
