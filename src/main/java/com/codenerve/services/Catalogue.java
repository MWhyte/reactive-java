package com.codenerve.services;

import com.codenerve.domain.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Catalogue {

    private static final Logger LOGGER = LogManager.getLogger(Catalogue.class.getName());

    public Product productByName(String productName) {
        LOGGER.info("productByName");
        return new Product(Product.Category.BOOK, "some book");
    }
}