package com.codenerve.services;

import com.codenerve.domain.Currency;
import com.codenerve.utils.Utils;

public class ExchangeService {

    public double lookupExchangeRate(Currency source, Currency destination) {
        return getRateWithDelay(source, destination);
    }

    private double getRateWithDelay(Currency source, Currency destination) {
        Utils.randomDelay();
        return source.rate / destination.rate;
    }
}
