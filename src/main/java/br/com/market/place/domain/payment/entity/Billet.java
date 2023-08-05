package br.com.market.place.domain.payment.entity;

import br.com.market.place.domain.customer.entity.Customer;
import br.com.market.place.domain.payment.boundary.ReadBilletOutputBoundary;
import br.com.market.place.domain.payment.constant.PaymentStatus;
import br.com.market.place.domain.payment.service.CancelPaymentService;
import br.com.market.place.domain.payment.service.RunPaymentService;
import br.com.market.place.domain.shared.exception.PaymentException;
import br.com.market.place.domain.shared.value.Address;
import br.com.market.place.domain.shared.value.Currency;
import br.com.market.place.domain.shared.value.DueDate;
import jakarta.persistence.Entity;

import static br.com.market.place.domain.payment.constant.PaymentStatus.PENDING;
import static br.com.market.place.domain.payment.constant.PaymentStatus.SUCCESS;

@Entity
public class Billet extends Payment {
    private String payLine;
    private DueDate dueDate;

    protected Billet() {
    }

    public Billet(Currency amount, Address address, PaymentStatus status, Customer customer, DueDate dueDate, String payLine) {
        super(amount, address, status, customer);
        this.dueDate = dueDate;
        this.payLine = payLine;
    }

    @Override
    public void pay(RunPaymentService payment) {
        PaymentStatus status = payment.executePayment(this);
        setStatus(status);
    }

    @Override
    public void cancelPayment(CancelPaymentService payment) {
        if (getStatus().itIsThat(SUCCESS, PENDING)) {
            var status = payment.cancelPayment(this);
            setStatus(status);
        } else {
            throw new PaymentException(String.format("Cannot cancel payment with status %s!", getStatus().name()));
        }
    }

    public String getPayLine() {
        return payLine;
    }

    public DueDate getDueDate() {
        return dueDate;
    }

    private void setPayLine(String payLine) {
        this.payLine = payLine;
    }

    private void setDueDate(DueDate dueDate) {
        this.dueDate = dueDate;
    }

    public ReadBilletOutputBoundary toReadBilletOutputBoundary() {
        return new ReadBilletOutputBoundary(
                getId().toString(),
                getDueDate().dateFormatted(),
                getCustomer().getDocument().document(),
                getCustomer().getDocument().documentType().name(),
                getPayLine(),
                getAmount().formatted()
        );
    }

    public static final class Builder {
        private Billet billet;

        private Builder() {
            billet = new Billet();
        }

        public static Builder build() {
            return new Builder();
        }

        public Builder withPayLine(String payLine) {
            billet.setPayLine(payLine);
            return this;
        }

        public Builder withDueDateExpireInDays(int days) {
            billet.setDueDate(new DueDate(days));
            return this;
        }

        public Builder withAmount(Currency amount) {
            billet.setAmount(amount);
            return this;
        }

        public Builder withAddress(Address address) {
            billet.setAddress(address);
            return this;
        }

        public Builder withPendingStatus() {
            billet.setStatus(PENDING);
            return this;
        }

        public Builder withCustomer(Customer customer) {
            billet.setCustomer(customer);
            return this;
        }

        public Billet now() {
            return new Billet(
                    billet.getAmount(),
                    billet.getAddress(),
                    billet.getStatus(),
                    billet.getCustomer(),
                    billet.getDueDate(),
                    billet.getPayLine()
            );
        }
    }
}
