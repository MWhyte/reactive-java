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

public class PriceCatalog {

    private static final Logger LOGGER = LogManager.getLogger(PriceCatalog.class.getName());
    private final Catalogue catalogue = new Catalogue();
    private final PriceFinder priceFinder = new PriceFinder();
    private final ExchangeService exchangeService = new ExchangeService();

    public static void main(String[] args) {
        new PriceCatalog().findLocalDiscountedPrice(Currency.CHF, "Nexus7");
    }

    private void findLocalDiscountedPrice(final Currency localCurrency, final String productName) {
        long time = System.currentTimeMillis();

        Product product = catalogue.productByName(productName);
        Price price = priceFinder.findBestPrice(product);

        double exchangeRate = exchangeService.lookupExchangeRate(Currency.USD, localCurrency);

        double localPrice = exchange(price, exchangeRate);

        LOGGER.info("A {} will cost us {} {}", productName, localPrice, localCurrency);
        LOGGER.info("It took us {} ms to calculate this", System.currentTimeMillis() - time);
    }

    private double exchange(Price price, double exchangeRate) {
        return Utils.round(price.getAmount() * exchangeRate);
    }
}