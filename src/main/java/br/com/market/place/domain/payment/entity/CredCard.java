package br.com.market.place.domain.payment.entity;

import br.com.market.place.domain.customer.entity.Customer;
import br.com.market.place.domain.payment.constant.PaymentStatus;
import br.com.market.place.domain.payment.service.CancelPaymentService;
import br.com.market.place.domain.payment.service.RunPaymentService;
import br.com.market.place.domain.payment.value.CardPan;

import br.com.market.place.domain.shared.exception.PaymentException;
import br.com.market.place.domain.shared.value.Address;
import br.com.market.place.domain.shared.value.Currency;
import jakarta.persistence.Entity;

@Entity
public class CredCard extends Payment {
    private CardPan cardPan;

    public CredCard(CardPan cardPan, Currency amount, Address address, PaymentStatus status, Customer customer) {
        super(amount, address, status,customer);
        this.cardPan = cardPan;
    }

    protected CredCard() {
        super();
    }


    @Override
    public void pay(RunPaymentService paymentService) {
        PaymentStatus status = paymentService.executePayment(this);
        setStatus(status);
    }

    @Override
    public void cancelPayment(CancelPaymentService payment) {
        if (getStatus().itIsThat(PaymentStatus.PENDING, PaymentStatus.SUCCESS)) {
            PaymentStatus status = payment.cancelPayment(this);
            if (status.itIsThat(PaymentStatus.SUCCESS))
                setStatus(PaymentStatus.CANCELED);
        } else {
            throw new PaymentException(String.format("Cannot cancel payment with status %s!", getStatus().name()));
        }
    }

    public CardPan getCardPan() {
        return cardPan;
    }

    private void setCardPan(CardPan cardPan) {
        this.cardPan = cardPan;
    }


    public static final class Builder {
        private CredCard credCard;

        private Builder() {
            credCard = new CredCard();
        }

        public static Builder build() {
            return new Builder();
        }

        public Builder withAmount(Currency amount) {
            credCard.setAmount(amount);
            return this;
        }

        public Builder withAddress(Address address) {
            credCard.setAddress(address);
            return this;
        }

        public Builder withCustomer(Customer customer) {
            credCard.setCustomer(customer);
            return this;
        }

        public Builder withCardPan(CardPan pan) {
            credCard.setCardPan(pan);
            return this;
        }


        public Builder withStatusPending() {
            credCard.setStatus(PaymentStatus.PENDING);
            return this;
        }


        public CredCard now() {
            return new CredCard(
                    credCard.getCardPan(),
                    credCard.getAmount(),
                    credCard.getAddress(),
                    credCard.getStatus(),
                    credCard.getCustomer()
            );
        }
    }
}
