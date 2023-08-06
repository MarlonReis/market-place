package br.com.market.place.domain.payment.constant;

import java.util.Arrays;

public enum PaymentStatus {
    PENDING,
    PAID_OUT,
    CANCELED,
    FAIL,
    EXPIRED,
    REVERSED;

    public boolean itIsThat(PaymentStatus ...status){
        return Arrays.asList(status).contains(this);
    }
}
