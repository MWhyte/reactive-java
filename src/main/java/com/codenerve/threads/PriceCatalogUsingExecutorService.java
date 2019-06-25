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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PriceCatalogUsingExecutorService {

    private static final Logger LOGGER = LogManager.getLogger(PriceCatalogUsingExecutorService.class.getName());
    private final Catalogue catalogue = new Catalogue();
    private final PriceFinder priceFinder = new PriceFinder();
    private final ExchangeService exchangeService = new ExchangeService();
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new PriceCatalogUsingExecutorService().findLocalDiscountedPrice(Currency.CHF, "Nexus7");
    }

    private void findLocalDiscountedPrice(final Currency localCurrency, final String productName) throws ExecutionException, InterruptedException {
        long time = System.currentTimeMillis();

        Future<Product> productFuture = executorService.submit(() -> catalogue.productByName(productName));
        Future<Price> priceFuture = executorService.submit(() -> priceFinder.findBestPrice(productFuture.get()));

        Future<Double> exchangeRateFuture = executorService.submit(() -> exchangeService.lookupExchangeRate(Currency.USD,
                localCurrency));

        double localPrice = exchange(priceFuture.get(), exchangeRateFuture.get());

        LOGGER.info("A {} will cost us {} {}", productName, localPrice, localCurrency);
        LOGGER.info("It took us {} ms to calculate this", System.currentTimeMillis() - time);

        executorService.shutdown();
    }

    private double exchange(Price price, double exchangeRate) {
        return Utils.round(price.getAmount() * exchangeRate);
    }
}