package br.com.market.place.domain.shared.constant;

import java.util.Locale;

public enum CurrencyType {
    USD(Locale.US),
    GBP(Locale.UK),
    BRL(Locale.forLanguageTag("PT-BR"));

    private final Locale country;

    CurrencyType(Locale country) {
        this.country = country;
    }

    public Locale country() {
        return country;
    }
}
