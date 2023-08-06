package br.com.market.place.domain.payment.boundary;

public record ReadBilletOutputBoundary(
        String paymentId,
        String dueDate,
        String document,
        String documentType,
        String fullName,
        String payLine,
        String amount,
        String status
) {
}
