package com.codenerve.domain;

public class Price {

    //PARTIALLY MOCKED OUT OBJECT

    private Currency pickRandomCurrencyForProduct;
    private double round;

    public Price(Currency pickRandomCurrencyForProduct, double round) {
        this.pickRandomCurrencyForProduct = pickRandomCurrencyForProduct;
        this.round = round;
    }

    public Integer getAmount() {
        return 10;
    }
}
