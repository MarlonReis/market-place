package br.com.market.place.domain.shared.constant;

import java.util.Locale;
import java.util.stream.Stream;

public enum CurrencyType {
    USD(Locale.US),
    GBP(Locale.UK),
    BRL(Locale.forLanguageTag("PT-BR"));

    private final Locale country;

    CurrencyType(Locale country) {
        this.country = country;
    }

    public static CurrencyType findByType(String type) {
        return Stream.of(values()).filter(va -> va.name().equalsIgnoreCase(type)).findFirst().orElseThrow();
    }

    public Locale country() {
        return country;
    }
}
