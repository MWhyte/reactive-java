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

public class PriceCatalogUsingCFBetter {

    private static final Logger LOGGER = LogManager.getLogger(PriceCatalogUsingCFBetter.class.getName());
    private final Catalogue catalogue = new Catalogue();
    private final PriceFinder priceFinder = new PriceFinder();
    private final ExchangeService exchangeService = new ExchangeService();

    public static void main(String[] args) {
        new PriceCatalogUsingCFBetter().findLocalDiscountedPrice(Currency.CHF, "Nexus7");
    }

    private void findLocalDiscountedPrice(final Currency localCurrency, final String productName) {
        long time = System.currentTimeMillis();

        lookupProduct(productName)
                .thenCompose(this::findBestPrice)
                .thenCombine(lookupExchange(localCurrency), this::exchange)
                .thenAccept(localPrice -> {
                    LOGGER.info("A {} will cost us {} {}", productName, localPrice, localCurrency);
                    LOGGER.info("It took us {} ms to calculate this", System.currentTimeMillis() - time);
                })
                .join();
                // only needed for this demo as the main thread dies before the results are returns.
                // a sleep would also fix this for this example.
    }

    private double exchange(Price price, double exchangeRate) {
        return Utils.round(price.getAmount() * exchangeRate);
    }

    private CompletableFuture<Product> lookupProduct(String productName) {
        return CompletableFuture.supplyAsync(() -> catalogue.productByName(productName));
    }

    private CompletableFuture<Price> findBestPrice(Product product) {
        return CompletableFuture.supplyAsync(() -> priceFinder.findBestPrice(product));
    }

    private CompletableFuture<Double> lookupExchange(Currency localCurrency) {
        return CompletableFuture.supplyAsync(() -> exchangeService.lookupExchangeRate(Currency.USD, localCurrency));
    }
}