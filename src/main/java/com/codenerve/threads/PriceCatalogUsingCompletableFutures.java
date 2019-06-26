package com.codenerve.threads;

import com.codenerve.domain.Currency;
import com.codenerve.domain.Price;
import com.codenerve.domain.Product;
import com.codenerve.services.Catalogue;
import com.codenerve.services.ExchangeService;
import com.codenerve.services.PriceFinder;
import com.codenerve.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;

public class PriceCatalogUsingCompletableFutures {

    private static final Logger LOGGER = LogManager.getLogger(PriceCatalogUsingCompletableFutures.class.getName());
    private final Catalogue catalogue = new Catalogue();
    private final PriceFinder priceFinder = new PriceFinder();
    private final ExchangeService exchangeService = new ExchangeService();

    public static void main(String[] args) {
        new PriceCatalogUsingCompletableFutures().findLocalDiscountedPrice(Currency.CHF, "Nexus7");
    }

    private void findLocalDiscountedPrice(final Currency localCurrency, final String productName) {
        long time = System.currentTimeMillis();

        CompletableFuture<Product> productCompletableFuture = CompletableFuture.supplyAsync(() -> catalogue.productByName(productName));
        CompletableFuture<Price> priceCompletableFuture = CompletableFuture.supplyAsync(() -> priceFinder.findBestPrice(productCompletableFuture.join()));
        CompletableFuture<Double> doubleCompletableFuture = CompletableFuture.supplyAsync(() -> exchangeService.lookupExchangeRate(Currency.USD, localCurrency));

        double localPrice = exchange(priceCompletableFuture.join(), doubleCompletableFuture.join());

        LOGGER.info("A {} will cost us {} {}", productName, localPrice, localCurrency);
        LOGGER.info("It took us {} ms to calculate this", System.currentTimeMillis() - time);
    }

    private double exchange(Price price, double exchangeRate) {
        return Utils.round(price.getAmount() * exchangeRate);
    }
}