package com.codenerve.domain;

public class Product {

    //PARTIALLY MOCKED OUT OBJECT

    public enum Category {
        BOOK, ELECTRONICS, HEALTH
    }

    private final Category category;
    private final String name;

    public Product(Category category, String name) {
        this.category = category;
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }
}