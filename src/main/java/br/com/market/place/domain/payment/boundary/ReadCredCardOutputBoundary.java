package br.com.market.place.domain.payment.boundary;

public record ReadCredCardOutputBoundary(
        String paymentId,
        String cardPan,
        String fullName,
        String document,
        String documentType,
        String amount,
        String status,
        String createAt
) { }
