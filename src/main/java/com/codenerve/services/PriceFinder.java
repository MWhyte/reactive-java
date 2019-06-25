package com.codenerve.services;

import com.codenerve.domain.Currency;
import com.codenerve.domain.Price;
import com.codenerve.domain.Product;
import com.codenerve.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PriceFinder {

    private static final Logger LOGGER = LogManager.getLogger(PriceFinder.class.getName());
    private final Currency[] currencyValues;

    public PriceFinder() {
        this.currencyValues = Currency.values();
    }

    public Price findBestPrice(Product product) {
        LOGGER.info("findBestPrice");

        return calculatePrice(product.getName());
    }

    private Price calculatePrice(String product) {
        Utils.randomDelay();

        double price = 10 * product.charAt(0) + product.charAt(1);

        return new Price(pickRandomCurrencyForProduct(product), Utils.round(price));
    }

    private Currency pickRandomCurrencyForProduct(String product) {
        // hack to avoid using Random
        return currencyValues[product.charAt(2) % currencyValues.length];
    }

}