package br.com.market.place.domain.payment.boundary;

public record ReadBilletOutputBoundary(
        String paymentId,
        String dueDate,
        String document,
        String documentType,
        String payLine,
        String amount
) {
}
