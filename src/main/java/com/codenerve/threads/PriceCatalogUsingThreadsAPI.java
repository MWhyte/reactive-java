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


public class PriceCatalogUsingThreadsAPI {

    private static final Logger LOGGER = LogManager.getLogger(PriceCatalogUsingThreadsAPI.class.getName());

    private final Catalogue catalogue = new Catalogue();
    private final PriceFinder priceFinder = new PriceFinder();
    private final ExchangeService exchangeService = new ExchangeService();

    public static void main(String[] args) throws InterruptedException {
        new PriceCatalogUsingThreadsAPI().findLocalDiscountedPrice(Currency.CHF, "Nexus7");
    }

    private void findLocalDiscountedPrice(final Currency localCurrency, final String productName) throws InterruptedException {
        long time = System.currentTimeMillis();

        PriceRunnable priceRunnable = new PriceRunnable(productName);
        ExchangeRunnable exchangeRunnable = new ExchangeRunnable(localCurrency);

        Thread priceThread = new Thread(priceRunnable);
        priceThread.start();

        Thread exchangeThread = new Thread(exchangeRunnable);
        exchangeThread.start();

        priceThread.join();
        exchangeThread.join();

        double localPrice = exchange(priceRunnable.getPrice(), exchangeRunnable.getExchangeRate());

        LOGGER.info("A {} will cost us {} {}", productName, localPrice, localCurrency);
        LOGGER.info("It took us {} ms to calculate this", System.currentTimeMillis() - time);
    }

    private double exchange(Price price, double exchangeRate) {
        return Utils.round(price.getAmount() * exchangeRate);
    }

    private class PriceRunnable implements Runnable {
        private String productName;
        private Price price;

        PriceRunnable(String productName) {
            this.productName = productName;
        }

        @Override
        public void run() {
            Product product = catalogue.productByName(productName);
            this.price = priceFinder.findBestPrice(product);
        }

        Price getPrice() {
            return price;
        }
    }

    private class ExchangeRunnable implements Runnable {
        private Currency localCurrency;
        private double exchangeRate;

        ExchangeRunnable(Currency localCurrency) {
            this.localCurrency = localCurrency;
        }

        @Override
        public void run() {
            this.exchangeRate = exchangeService.lookupExchangeRate(Currency.USD, localCurrency);
        }

        double getExchangeRate() {
            return exchangeRate;
        }
    }
}