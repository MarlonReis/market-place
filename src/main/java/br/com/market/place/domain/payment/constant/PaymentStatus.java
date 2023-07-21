package br.com.market.place.domain.payment.constant;

import java.util.Arrays;

public enum PaymentStatus {
    PENDING,
    SUCCESS,
    CANCELED,
    FAIL,
    EXPIRED;

    public boolean itIsThat(PaymentStatus ...status){
        return Arrays.asList(status).contains(this);
    }
}
